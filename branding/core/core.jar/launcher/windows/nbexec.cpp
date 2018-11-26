/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 * 
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <io.h>
#include <fcntl.h>
#include <process.h>
#include <commdlg.h>

#define PROG_FULLNAME "Error"
#define IDE_MAIN_CLASS "org/netbeans/Mains"
#define UPDATER_MAIN_CLASS "org/netbeans/updater/UpdaterFrame"

#define JDK_KEY "Software\\JavaSoft\\Java Development Kit"
#define JRE_KEY "Software\\JavaSoft\\Java Runtime Environment"

#define RUN_NORMAL "-run_normal"
#define RUN_UPDATER "-run_updater"

#define BAD_OPTION_MSG "Wrong option."

static char jdkhome[MAX_PATH];
static char plathome[MAX_PATH];
static char userdir[MAX_PATH];

static char clusters[MAX_PATH * 20];

static char *bootclass = NULL;

static int runnormal = 0;
static int runupdater = 0;

static char classpath[1024 * 16];
static char classpathBefore[1024 * 16];
static char classpathAfter[1024 * 16];

static char **options;
static int numOptions, maxOptions;

static char *progArgv[1024];
static int progArgc = 0;

static void runClass(char *mainclass);

static void fatal(const char *str);
static int findJdkFromRegistry(const char* keyname, char jdkhome[]);

static void addJdkJarsToClassPath(const char *jdkhome);
static void addLauncherJarsToClassPath(const char *plathome);

static void addToClassPath(const char *pathprefix, const char *path);
static void addToClassPathIfExists(const char *pathprefix, const char *path);
static void addAllFilesToClassPath(const char *dir, const char *pattern);

static void parseArgs(int argc, char *argv[]);
static void addOption(char *str);

static char* createOsEnvFile();
static int fileExists(const char* path);

static void normalizePath(char *userdir);
static bool runAutoUpdater(bool firstStart, const char * root);
static bool runAutoUpdaterOnClusters(bool firstStart);

static int findHttpProxyFromRegistry(char **proxy, char **nonProxy);

int main(int argc, char *argv[]) {
    char exepath[1024 * 4];
    char buf[1024 * 8], *pc;
  
	GetModuleFileName(0, buf, sizeof buf);
    strcpy(exepath, buf);

    pc = strrchr(buf, '\\');
    if (pc != NULL) {             // always holds
        strlwr(pc + 1);
        *pc = '\0';	// remove .exe filename
    }

    pc = strrchr(buf, '\\');
    if (pc != NULL && 0 == stricmp("\\lib", pc))
        *pc = '\0';
    strcpy(plathome, buf);
    strcpy(clusters, buf);

    if (0 != findJdkFromRegistry(JDK_KEY, jdkhome))
        findJdkFromRegistry(JRE_KEY, jdkhome);
    
    parseArgs(argc - 1, argv + 1); // skip progname

    if (!runnormal && !runupdater) {
        char **newargv = (char**) malloc((argc+10) * sizeof (char*));
        int i;
            
        if (userdir[0] == '\0') {
            fatal("Need to specify userdir using command line option --userdir");
            exit(1);
        }
        
        sprintf(buf, "\"%s\"", argv[0]);
        newargv[0] = strdup(buf);
        
        for (i = 1; i < argc; i++) {
            sprintf(buf, "\"%s\"", argv[i]);
            newargv[i+1] = strdup(buf);
        }
        i++;
            
        if (userdir[0] != '\0') {
            newargv[i++] = "-userdir";
                
            strcat(strcat(strcpy(buf, "\""), userdir), "\"");
            newargv[i++] = strdup(buf);
        }

        char *envfile = createOsEnvFile();
        if (envfile != NULL) {
            sprintf(buf, "\"%s=%s\"", "-J-Dnetbeans.osenv", envfile);
            newargv[i++] = strdup(buf);
            newargv[i++] = strdup("\"-J-Dnetbeans.osenv.nullsep=true\"");
        }
            
        newargv[i] = NULL;
            
        // check for patches first

        bool runUpdater = runAutoUpdaterOnClusters(true);

        if (runUpdater) {
            newargv[1] = RUN_UPDATER;
            _spawnv(_P_WAIT, exepath, newargv);
        }

      AGAIN:

        // run IDE

        newargv[1] = RUN_NORMAL;
        _spawnv(_P_WAIT, exepath, newargv);

        // check for patches again
        
        runUpdater = runAutoUpdaterOnClusters(false);
        if (runUpdater) {
            newargv[1] = RUN_UPDATER;
            _spawnv(_P_WAIT, exepath, newargv);
            
            goto AGAIN;
        }
        _unlink(envfile);
    } else if (runnormal) {
        argc -= 2;
        argv += 2;
        if (bootclass != NULL) {
            runClass(bootclass);
        }
        else {
            runClass(IDE_MAIN_CLASS);
        }
    } else if (runupdater) {
        argc -= 2;
        argv += 2;
        runClass(UPDATER_MAIN_CLASS);
    }

    return 0;
}

bool runAutoUpdaterOnClusters(bool firstStart) {
    bool runUpdater = false;
    char *pc, buf[sizeof clusters];

    runUpdater = runAutoUpdater(firstStart, plathome);
    
    strcpy(buf, clusters);
    pc = strtok(buf, ";");
    while (pc != NULL) {
        runUpdater |= runAutoUpdater(firstStart, pc);
        pc = strtok(NULL, ";");
    }
    runUpdater |= runAutoUpdater(firstStart, userdir);
    
    return runUpdater;
}

bool runAutoUpdater(bool firstStart, const char * root) {
    WIN32_FIND_DATA ffd;
    char tmp [MAX_PATH];

    strcpy(tmp, root);
    strcat(tmp, "\\update\\download\\*.nbm");

    if (INVALID_HANDLE_VALUE == FindFirstFile(tmp, &ffd))
        return false;

    // some *.nbm files exist

    strcpy(tmp, root);
    strcat(tmp, "\\update\\download\\install_later.xml");

    bool found = INVALID_HANDLE_VALUE != FindFirstFile(tmp, &ffd);

    return (found && firstStart) || (!firstStart && !found);
}

void runClass(char *mainclass) {
    char buf[10240];

    if (jdkhome[0] == '\0')
        fatal("J2SE 1.4 or later cannot be found on your machine.");

    strcat(strcpy(buf, "-Djdk.home="), jdkhome);
    addOption(buf);
    
    strcat(strcpy(buf, "-Dnetbeans.home="), plathome);
    addOption(buf);

    strcat(strcpy(buf, "-Dnetbeans.dirs="), clusters);
    addOption(buf);
    
    if (userdir[0] != '\0') {
        strcat(strcpy(buf, "-Dnetbeans.user="), userdir);
        addOption(buf);
    }
  
    if (classpathBefore[0] != '\0')
        strcpy(classpath, classpathBefore);
    else
        classpath[0] = '\0';
  
    addLauncherJarsToClassPath(plathome);
    addJdkJarsToClassPath(jdkhome);

    char *proxy, *nonProxyHosts;
    if (0 == findHttpProxyFromRegistry(&proxy, &nonProxyHosts)) {
        sprintf(buf, "-Dnetbeans.system_http_proxy=%s", proxy);
        addOption(buf);
        sprintf(buf, "-Dnetbeans.system_http_non_proxy_hosts=%s", nonProxyHosts);
        addOption(buf);
        free(proxy);
        free(nonProxyHosts);
    }

    // see BugTraq #5043070
    addOption("-Dsun.awt.keepWorkingSetOnMinimize=true");
    
    char javapath[MAX_PATH];
    WIN32_FIND_DATA ffd;

    strcat(strcpy(javapath, jdkhome), "\\jre\\bin\\java.exe");
    if (INVALID_HANDLE_VALUE == FindFirstFile(javapath, &ffd)) {
        strcat(strcpy(javapath, jdkhome), "\\bin\\java.exe");
        if (INVALID_HANDLE_VALUE == FindFirstFile(javapath, &ffd)) {
	    sprintf(buf, "Cannot find java.exe in %s\\...", jdkhome); 
            fatal(buf);
        }
    }

    int argc = numOptions + 3 + progArgc + 1;
    char **args = (char**) malloc(argc * sizeof(char*));
    char **p = args;

    sprintf(buf, "\"%s\"", javapath);
    *p++ = strdup(buf);

    int i;    
    for (i = 0; i < numOptions; i++) {
        sprintf(buf, "\"%s\"", options[i]);
        *p++ = strdup(buf);
    }

    *p++ = "-cp";

    if (classpathAfter[0] != '\0') {
        if (classpath[strlen(classpath)] != ';')
            strcat(classpath, ";");
        strcat(classpath, classpathAfter);
    }
    sprintf(buf, "\"%s\"", classpath);
    *p++ = strdup(buf);
    
    *p++ = mainclass;

    for (i = 0; i < progArgc; i++) {
        sprintf(buf, "\"%s\"", progArgv[i]);
        *p++ = strdup(buf);
    }
    *p++ = NULL;

    // Prevents Windows from bringing up a "No floppy in drive A:" dialog.
    // The error mode should be inherited by the java process
    SetErrorMode(SetErrorMode(0) | SEM_FAILCRITICALERRORS | SEM_NOOPENFILEERRORBOX);
    
    _spawnv(_P_WAIT, javapath, args);
}

//////////

/*
 * Returns string data for the specified registry value name, or
 * NULL if not found.
 */
static char * GetStringValue(HKEY key, const char *name)
{
    DWORD type, size;
    char *value = 0;

    if (RegQueryValueEx(key, name, 0, &type, 0, &size) == 0 && type == REG_SZ) {
        value = (char*) malloc(size);
        if (RegQueryValueEx(key, name, 0, 0, (unsigned char*)value, &size) != 0) {
            free(value);
            value = 0;
        }
    }
    return value;
}

static int findJdkFromRegistry(const char* keyname, char jdkhome[])
{
    HKEY hkey = NULL, subkey = NULL;
    char *ver = NULL;
    int rc = 1;
  
    if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, keyname, 0, KEY_READ, &hkey) == 0) {
        ver = GetStringValue(hkey, "CurrentVersion");
        if (ver == NULL)
            goto quit;

        if (RegOpenKeyEx(hkey, ver, 0, KEY_READ, &subkey) == 0) {
            char *home = GetStringValue(subkey, "JavaHome");
            if (home == NULL)
                goto quit;
            strcpy(jdkhome, home);
            free(home);
            rc = 0;
        }
    }

  quit:
    if (ver != NULL)
        free(ver);
    if (subkey != NULL)
        RegCloseKey(subkey);
    if (hkey != NULL)
        RegCloseKey(hkey);
    return rc;
}

int findHttpProxyFromRegistry(char **proxy, char **nonProxy)
{
    HKEY hkey = NULL;
    char *proxyServer = NULL;
    char *proxyOverrides = NULL;
    int rc = 1;
    *proxy = NULL; *nonProxy = NULL;
  
    if (RegOpenKeyEx(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Internet settings", 0, KEY_READ, &hkey) == 0) {
        DWORD proxyEnable, size = sizeof proxyEnable;

        if (RegQueryValueEx(hkey, "ProxyEnable", 0, 0, (unsigned char*) &proxyEnable, &size) == 0) {
            if (proxyEnable) {
                proxyServer = GetStringValue(hkey, "ProxyServer");
                if (proxyServer == NULL)
                    goto quit;

                if (strstr(proxyServer, "=") == NULL) {
                    *proxy = strdup(proxyServer);
                    rc = 0;
                } else {
                    char *pc = strstr(proxyServer, "http=");
                    if (pc != NULL) {
                        pc += strlen("http=");
                        char *qc = strstr(pc, ";");
                        if (qc != NULL)
                            *qc = '\0';
                        *proxy = strdup(pc);
                        rc = 0;
                    }
                }
                // ProxyOverride contains semicolon delimited list of host name prefixes 
                // to connect them directly w/o proxy
                // optionally last entry is <local> to bypass local addresses
                // *acme.com also works there
                proxyOverrides = GetStringValue(hkey, "ProxyOverride");
                if (proxyOverrides != NULL)
                    *nonProxy = strdup(proxyOverrides);
                else 
                    *nonProxy = strdup("");
            }
	    else {
	        *proxy = strdup("DIRECT");
	        *nonProxy = strdup("");
	        rc = 0;
            } 
        }
    }

  quit:
    if (proxyServer != NULL)
        free(proxyServer);
    if (proxyOverrides != NULL)
        free(proxyOverrides);
    if (hkey != NULL)
        RegCloseKey(hkey);
    return rc;
}

void addToClassPath(const char *pathprefix, const char *path) {
    char buf[1024];
    
    strcpy(buf, pathprefix);
    if (path != NULL)
        strcat(strcat(buf, "\\"), path);

    if (classpath[0] != '\0')
        strcat(classpath, ";");
    strcat(classpath, buf);
}

void addToClassPathIfExists(const char *pathprefix, const char *path) {
    char buf[1024];
 
    strcpy(buf, pathprefix);
    if (path != NULL)
        strcat(strcat(buf, "\\"), path);

    if (fileExists(buf) != 0)
        addToClassPath(pathprefix, path);
}

  
void addJdkJarsToClassPath(const char *jdkhome)
{
    addToClassPathIfExists(jdkhome, "lib\\dt.jar");
    addToClassPathIfExists(jdkhome, "lib\\tools.jar");
}

void addLauncherJarsToClassPath(const char *plathome)
{
    char buf[1024];

    strcat(strcpy(buf, plathome), "\\lib");
    addAllFilesToClassPath(buf, "*.jar");
    addAllFilesToClassPath(buf, "*.zip");

    strcat(strcpy(buf, plathome), "\\lib\\locale");
    addAllFilesToClassPath(buf, "*.jar");
    addAllFilesToClassPath(buf, "*.zip");

    if (runupdater) {
        addToClassPath(plathome, "\\modules\\ext\\updater.jar");
        strcat(strcpy(buf, plathome), "\\modules\\ext\\locale");
        addAllFilesToClassPath(buf, "updater_*.jar");
    }
}

void addAllFilesToClassPath(const char *dir,
                            const char *pattern) {
    char buf[1024];
    struct _finddata_t fileinfo;
    long hFile;

    strcat(strcat(strcpy(buf, dir), "\\"), pattern);
  
    if ((hFile = _findfirst(buf, &fileinfo)) != -1L) {
        addToClassPath(dir, fileinfo.name);

        while (0 == _findnext(hFile, &fileinfo))
            addToClassPath(dir, fileinfo.name);
    
        _findclose(hFile);
    }
}

void fatal(const char *str)
{
//#ifdef WINMAIN
    ::MessageBox(NULL, str, PROG_FULLNAME, MB_ICONSTOP | MB_OK);
//#else  
//  fprintf(stderr, "%s\n", str);
//#endif
    exit(255);
}

/*
 * Adds a new VM option with the given given name and value.
 *
 * Doesn't modify the input string, creates its copy.
 *
 */
void addOption(char *str)
{
    /*
     * Expand options array if needed to accomodate at least one more
     * VM option.
     */
    if (numOptions >= maxOptions) {
        if (options == 0) {
            maxOptions = 4;
            options = (char**) malloc(maxOptions * sizeof(char*));
        } else {
            char** tmp;
            maxOptions *= 2;
            tmp = (char**)malloc(maxOptions * sizeof(char*));
            memcpy(tmp, options, numOptions * sizeof(char*));
            free(options);
            options = tmp;
        }
    }
    options[numOptions++] = strdup(str);
}

void parseArgs(int argc, char *argv[]) {
    char *arg;

    while (argc > 0 && (arg = *argv) != 0) {
        argv++;
        argc--;

        if ((strcmp("-h", arg) == 0
            || strcmp("-help", arg) == 0
            || strcmp("--help", arg) == 0
            || strcmp("/?", arg) == 0
            ) && runnormal) {
            fprintf(stdout, "Usage: launcher {options} arguments\n\
\n\
General options:\n\
  --help                show this help\n\
  --jdkhome <path>      path to J2SE JDK\n\
  -J<jvm_option>        pass <jvm_option> to JVM\n\
\n\
  --cp:p <classpath>    prepend <classpath> to classpath\n\
  --cp:a <classpath>    append <classpath> to classpath\n\
\n");  
            fflush(stdout);
            arg = "--help";
        }
        

        if (0 == strcmp("-userdir", arg) || 0 == strcmp("--userdir", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    strcpy(userdir, arg);
                    normalizePath(userdir);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strcmp("--clusters", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    strcpy(clusters, arg);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strcmp("--bootclass", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    bootclass = strdup(arg);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strcmp(RUN_NORMAL, arg)) {
            runnormal = 1;
            runupdater = 0;
        } else if (0 == strcmp(RUN_UPDATER, arg)) {
            runnormal = 0;
            runupdater = 1;
        } else if (0 == strcmp("-jdkhome", arg) || 0 == strcmp("--jdkhome", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    strcpy(jdkhome, arg);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strcmp("-cp:p", arg) || 0 == strcmp("--cp:p", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    if (classpathBefore[0] != '\0'
                        && classpathBefore[strlen(classpathBefore)] != ';')
                        strcat(classpathBefore, ";");
                    strcat(classpathBefore, arg);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strcmp("-cp", arg) || 0 == strcmp("-cp:a", arg) || 0 == strcmp("--cp", arg) || 0 == strcmp("--cp:a", arg)) {
            if (argc > 0) {
                arg = *argv;
                argv++;
                argc--;
                if (arg != 0) {
                    if (classpathAfter[0] != '\0'
                        && classpathAfter[strlen(classpathAfter)] != ';')
                        strcat(classpathAfter, ";");
                    strcat(classpathAfter, arg);
                }
            }
            else {
                fatal(BAD_OPTION_MSG);
            }
        } else if (0 == strncmp("-J", arg, 2)) {
            addOption(arg + 2);
        } else {
            progArgv[progArgc++] = arg;
        }
    }
}

void normalizePath(char *userdir) {
    char buf[MAX_PATH], *pc;

    // absolutize userdir
    if (NULL == _fullpath(buf, userdir, MAX_PATH))
        return;
    
    userdir[0] = '\0';

    if (buf[0] == '\\' && buf[1] == '\\') { // UNC share
        userdir[0] = '\\';
        userdir[1] = '\\';
        userdir[2] = '\0';
        pc = strtok(buf + 2, "/\\");
    } else {
        pc = strtok(buf, "/\\");
    }
  
    while (pc != NULL) {
        if (*pc != '\0') {
            if (userdir[0] != '\0' && userdir[strlen(userdir) - 1] != '\\')
                strcat(userdir, "\\");
            strcat(userdir, pc);
        }
        pc = strtok(NULL,  "/\\");
    }
    if (userdir[1] == ':' && userdir[2] == '\0')
        strcat(userdir, "\\");
}

/* Send env vars to Java process. NOTE: ignored on JDK 1.5; useful only on JDK 1.4. */
char* createOsEnvFile() {
    char systemDir[MAX_PATH];
    if (userdir[0] != '\0') {
        strcpy(systemDir, userdir);
    }
    else {
        strcpy(systemDir, plathome);
    }
    strcat(systemDir, "\\var");
    char *tmpname = _tempnam(systemDir, "nbenv");
    if (tmpname == NULL)
        return NULL;

    if (_environ == NULL)
        return NULL;
    
    FILE *of = fopen(tmpname, "w");
    if (of == NULL)
        return NULL;
    
    for (char **pe = _environ; *pe != NULL; pe++) {
        fprintf(of, "%s", *pe);
        fputc('\0', of);
    }
    fclose(of);

    return strdup(tmpname);
}


int fileExists(const char* path) {
    WIN32_FIND_DATA ffd;
    HANDLE ffh;
    
    memset(&ffd, 0, sizeof ffd);
    ffh = FindFirstFile(path, &ffd);
    if (ffh != INVALID_HANDLE_VALUE) {
        FindClose(ffh);
        return 1;
    } else {
        return 0;
    }
}
