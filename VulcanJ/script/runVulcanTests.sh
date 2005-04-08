#!/bin/sh
###
##
## Run all of the Vulcan Junit tests on chaos (linux host)
## 
###
export ANT_HOME=/usr/local/ant/ant1.6.2
export CJ_HOME=$HOME/client-java
export VJ_HOME=$HOME/VulcanJ
export JAVA_HOME=/usr/java/j2sdk1.4.2_02
export VULCAN=/usr/local/vulcan
export VULCAN_CONF=$HOME/vulcanlnx.conf
export LD_LIBRARY_PATH=$VULCAN/bin:$CJ_HOME/output
export PATH=$PATH:${ANT_HOME}/bin:${VULCAN}/bin:/usr/local/bin:/bin
export CLASSPATH=$CJ_HOME/output/lib/firebirdsql-full.jar:$HOME/lib/junit.jar:$HOME/lib/log4j-1.2.8.jar:$HOME/lib/GroboTestingJUnit-1.2.1-core.jar

myDate=`date '+%Y-%m-%d'`

set > $HOME/chaos-env.txt

# client-java tests
cd $CJ_HOME
rm -f fbtest.gdb
rm -f output/db/fbtest.gdb
./build.sh clean
./build.sh -Dtest.db.provider_url=file://localhost/u/bioliv/client-java/datasources -Dbuild.reports.embedded=/u/bioliv/public/client-java-reports-$myDate/lnx/vul tests-report-html-embedded
# copy the newly built JNI over
cp $CJ_HOME/output/native/libjaybird.so $VJ_HOME/lib
cp $CJ_HOME/output/lib/firebirdsql-full.jar $VJ_HOME/lib

# run the VulcanJ tests
cd $VJ_HOME
rm -f test.fdb
ant -Dtest.gds_type=EMBEDDED -Dtest.reports=/u/bioliv/public/vulcanj-reports-$myDate/lnx/vul
