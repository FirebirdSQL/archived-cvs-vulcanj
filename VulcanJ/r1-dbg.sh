if [ $# -eq 0 ] ; then
   echo "$0, a utility to execute a testrunner for VulcanJ"
   echo "\tYOU ARE USING THE REMOTE DEBUG VERSION!!!"
   echo "\tUsage: $0 classname"
   echo "\tExample: $0 org.firebirdsql.nist.TestYts"
else
   export CLASSPATH=$CLASSPATH:./classes
   java -Xdebug -Xrunjdwp:transport=dt_socket,address=2000,server=y,suspend=y -Dtest.gds_type=EMBEDDED -Dtest.db.dir=. -DFBLog4j=true -Dlog4j.configuration=log4j.lcf  junit.textui.TestRunner $1
fi
