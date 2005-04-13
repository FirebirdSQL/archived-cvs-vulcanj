REM
REM Script to run VulcanJ and client-java tests.
REM

REM get latest vulcan build
copy /y \\newwinsrc\sasgen\dev\mva\vulcan\com\w32nd\* c:\sasv9\vulcan\bin
REM copy /y \\dntsrc\u\sassek\vulcan3\vulcan\com\w32nd\* c:\sasv9\vulcan\bin

set VUL_PATH=c:\sasv9\vulcan\bin;c:\workspace\client-java\output\native
set CURR_PATH=%PATH%

pushd \workspace\firebird\vulcanj

@echo off
rem Create the date and time elements.
rem http://www.winnetmag.com/WindowsScripting/Article/ArticleID/9177/9177.html
For /f "tokens=1-7 delims=:/-, " %%i in ('echo exit^|cmd /q /k"prompt $D $T"') do (
	For /f "tokens=2-4 delims=/-,() skip=1" %%a in ('echo.^|date') do (
		set dow=%%i
		set %%a=%%j
		set %%b=%%k
		set %%c=%%l
		set hh=%%m
		set min=%%n
		set ss=%%o
	)
)
@echo on

REM
REM Vulcan Tasks
REM
set path=%VUL_PATH%;%CURR_PATH%
del test.fdb
call ant -Dtest.reports=\\dntsrc\bioliv\public\vulcanj-reports-%yy%-%mm%-%dd%\win\vul

REM
REM client-java tests
REM
popd
pushd \workspace\client-java
call "C:\Program Files\Microsoft Visual Studio .NET 2003\Common7\Tools\vsvars32.bat"
set VS_PATH=%PATH%
set path=%VUL_PATH%;%VS_PATH%
call build.bat -Dtest.db.dir=/workspace/client-java/output/db -Dbuild.reports.embedded=\\dntsrc\bioliv\public\client-java-reports-%yy%-%mm%-%dd%\win\vul tests-report-html-embedded

:Done
popd
set path=%CURR_PATH%
