#!/usr/local/bin/bash

#
# This script executes all of the .sql files in the directory
# tree,  using "$sqldir" variable.
#

function process_dir {
   for f in $1/*
   do
      if [ -d "$f" ]; then
         # skip processing of special directories
         # echo directory processing..
         mydir=${f##*ddl/}
         mydir=${mydir##*/}
         # echo dir is ${mydir}
         if [ ${mydir} != input ] && [ ${mydir} != draft ] && [ ${mydir} != security ] && [ ${mydir} != CVS ]; then
            process_dir "$f"
         fi
      else
         case "$f" in
          *.sql)
             if [ $verbose = true ]; then
                echo "$f"
             else
                echo -n '.'
             fi
             # f can be /net/bin/u/bioliv/VulcanJ/ddl/cast/cast_01.sql
             # f can also be ./ddl/cast/cast_01.sql
             
             # myfile is file w/o path info
             myfile=${f##*/}

             blgfile=${f%%ddl*}${blgdir}${f##*ddl}
             blgfile=${blgfile%.*}.blg
             outfile=${f%%ddl*}${outdir}${f##*ddl}
             outfile=${outfile%.*}.output
             difffile=${f%%ddl*}diff/${myfile%.*}.diff
             # echo outfile $outfile
             # echo blgfile $blgfile
             # echo difffile $difffile
             # echo

             if [ ! -e "$outdir" ]; then
                if [ $verbose = true ]; then
                   echo creating directory "$outdir"
                fi
                mkdir "$outdir"
             fi
             if [ -e test.fdb ]; then
                rm test.fdb
             fi

             rm -f "$outfile" "$outfile"
             if [ $time_each_test = true ]; then
                st=`date '+%s.%N'`
                isql -i "$f" -o "$outfile" -m -e
                en=`date '+%s.%N'`
                echo "$f","$st","$en" >> timings.txt
             else
                isql -i "$f" -o "$outfile" -m -e
             fi

             let numTests=$numTests+1
             rm -f new.txt old.txt
             if [ ${outfile%%/bin*} = /net ]; then
                outfile=/net/txt${outfile#/net/bin*}
                blgfile=/net/txt${blgfile#/net/bin*}
                difffile=/net/txt${difffile#/net/bin*}
                # echo fixed ${outfile}
                # echo fixed ${blgfile}
             fi
             sed -f ${filter} "$outfile" > new.txt
             sed -f ${filter} "$blgfile" > old.txt
             diff -b old.txt new.txt > "$difffile"
             # exit 1 
             if [ ! -s "$difffile" ]; then
                rm -f "$difffile"
             else
               echo
               echo "   diff detected! : "$difffile""
               let failedTests=$failedTests+1
             fi
          ;;
         esac
      fi
   done
}

filter=filter.sed
diffdir=diff
sqldir=ddl
outdir=output
blgdir=blg-vulcan
numTests=0
failedTests=0
verbose=false
start_tm=0
end_tm=0
time_each_test=false

echo "beginning fbmust tests..."

while [ -n "$(echo $1 | grep '-')" ]; do
   case $1 in
      -b ) blgdir="$2"
           shift
           ;;
      -d ) sqldir="$2"
           shift
           ;;
      -s ) filter="$2"
           shift
           ;;
      -v ) verbose='true'
           ;;
      -t ) time_each_test='true'
           ;;
      *  ) echo 'usage: fbmust [-v] [-m] [-d directory] [-o directory]'
           echo 'example: '
           echo '   fbmust -v -d ./ddl/nist'
           echo 'mvs example:'
           echo '   cd /net/txt/u/bioliv/VulcanJ'
           echo '   /net/txt/u/bioliv/VulcanJ/fbmust.sh -v -d /net/bin/u/bioliv/fbmust/ddl -s /net/txt/u/bioliv/fbmust/ddl/filter.sed'

           exit 1
   esac
   shift
done

if [ ! -e "$outdir" ]; then
   if [ $verbose = true ]; then
      echo creating directory "$outdir"
   fi
   mkdir "$outdir"
fi

if [ ! -e "$diffdir" ]; then
   if [ $verbose = true ]; then
      echo creating directory "$diffdir"
   fi
   mkdir "$diffdir"
fi


if [ $verbose = true ]; then
   echo outdir: "$outdir"
   echo diffdir: "$diffdir"
   echo sqldir: "$sqldir"
fi

# clean out old diff files, quietly
rm -f timings.txt
start_tm=`date '+%s'`
rm -f "$diffdir"/*
process_dir "$sqldir"
echo

if [ $verbose = true ]; then
   end_tm=`date '+%s'`
fi

# cleanup
rm -f new.txt old.txt


echo "$numTests" tests were executed. "$failedTests" failed.
if [ $failedTests -gt 0 ]; then
   echo Output files are in ./output, diff files are in ./diff
fi

end_tm=`date '+%s'`
let duration="$end_tm"-"$start_tm"
echo Tests took "$duration" seconds.
