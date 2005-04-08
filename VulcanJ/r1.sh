if [ $# -eq 0 ] ; then
   echo "$0, a utility to execute a testrunner for VulcanJ"
   echo "\tUsage: $0 classname"
   echo "\tExample: $0 org.firebirdsql.nist.TestYts"
else
   echo $LD_LIBRARY_PATH
   export CLASSPATH=$CLASSPATH:./classes
   java -DFBLog4j=true -Dlog4j.configuration=file:./lib/log4j-console.lcf junit.textui.TestRunner $1
fi