!include Library.nsh
 
  Var ALREADY_INSTALLED
 
 Section "-Install VB6 runtimes"
 
   ;Add code here that sets $ALREADY_INSTALLED to a non-zero value if the
   ;application is already installed. For example:
 
   IfFileExists "$INSTDIR\MyApp.exe" 0 new_installation
   ;Replace MyApp.exe with your application filename
     StrCpy $ALREADY_INSTALLED 1
   new_installation:
 
   !insertmacro InstallLib REGDLL $ALREADY_INSTALLED REBOOT_NOTPROTECTED \
     "msvbvm60.dll" "$SYSDIR\msvbvm60.dll" "$SYSDIR"
   !insertmacro InstallLib REGDLL $ALREADY_INSTALLED REBOOT_PROTECTED \
     "oleaut32.dll" "$SYSDIR\oleaut32.dll" "$SYSDIR"
   !insertmacro InstallLib REGDLL $ALREADY_INSTALLED REBOOT_PROTECTED \
     "olepro32.dll" "$SYSDIR\olepro32.dll" "$SYSDIR"
   !insertmacro InstallLib REGDLL $ALREADY_INSTALLED REBOOT_PROTECTED \
     "comcat.dll"   "$SYSDIR\comcat.dll"   "$SYSDIR"
   !insertmacro InstallLib DLL    $ALREADY_INSTALLED REBOOT_PROTECTED \
     "asycfilt.dll" "$SYSDIR\asycfilt.dll" "$SYSDIR"
   !insertmacro InstallLib TLB    $ALREADY_INSTALLED REBOOT_PROTECTED \
     "stdole2.tlb"  "$SYSDIR\stdole2.tlb"  "$SYSDIR"
 
 SectionEnd
 
 Section "-un.Uninstall VB6 runtimes"
 
   !insertmacro UnInstallLib REGDLL SHARED NOREMOVE "$SYSDIR\msvbvm60.dll"
   !insertmacro UnInstallLib REGDLL SHARED NOREMOVE "$SYSDIR\oleaut32.dll" 
   !insertmacro UnInstallLib REGDLL SHARED NOREMOVE "$SYSDIR\olepro32.dll"
   !insertmacro UnInstallLib REGDLL SHARED NOREMOVE "$SYSDIR\comcat.dll" 
   !insertmacro UnInstallLib DLL    SHARED NOREMOVE "$SYSDIR\asycfilt.dll"
   !insertmacro UnInstallLib TLB    SHARED NOREMOVE "$SYSDIR\stdole2.tlb"
 
 SectionEnd