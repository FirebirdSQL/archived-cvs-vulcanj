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
         # echo ${f##.*/}
         if [ ${f##./ddl/} != input ] && [ ${f##./ddl/} != draft ]; then
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
             # f is ./ddl/avg/avg_01.sql
             # myfile holds the file name + suffix
             myfile=${f##.*/}

             # transform ./ddl/avg/avg_01.sql to
             #    /net/txt/u/bioliv/VulcanJ/blg-vulcan/avg/avg_01.blg

             # change the suffix
             blgfile=${f%.sql}.blg
             # remove the ./dll/ and add correct prefix
             # blgfile=${blgfile##.*ddl}
             # add the correct prefix
             blgfile=${prefix}blg-vulcan${blgfile##.*ddl}

             # change the suffix
             outfile=${f%.sql}.output
             #outfile=${prefix}output${outfile##.*ddl}
             outfile=${prefix}output${outfile##.*ddl}
             difffile=${prefix}diff/${myfile%.sql}.diff

             #echo outfile $outfile
             #echo blgfile $blgfile
             #echo difffile $difffile
             #echo

             outdir=${outfile%/*}
             if [ ! -e "$outdir" ]; then
                if [ $verbose = true ]; then
                   echo creating directory "$outdir"
                fi
                mkdir "$outdir"
             fi
             #echo "$outdir"
             #echo blg file is "$blgfile", output file is "$outfile"
             if [ -e test.fdb ]; then
                rm test.fdb
             fi

             if [ $mvs = true ]; then
                rm -f "$outfile" "$outfile"_ascii
                isql -i "$f" -o "$outfile"_ascii -m -e
                iconv -f ISO8859-1 -t IBM-1047 "$outfile"_ascii > "$outfile"
             else
                rm -f "$outfile" "$outfile"
                if [ $time_each_test = true ]; then
                   st=`date '+%s.%N'`
                   isql -i "$f" -o "$outfile" -m -e
                   en=`date '+%s.%N'`
                   echo "$f","$st","$en" >> timings.txt
                else
                   isql -i "$f" -o "$outfile" -m -e
                fi
             fi

             let numTests=$numTests+1
             rm -f new.txt old.txt
             sed -f filter.sed "$outfile" > new.txt
             sed -f filter.sed "$blgfile" > old.txt
             # diff -b "$blgfile" "$outfile" > "$difffile"
             diff -bB old.txt new.txt > "$difffile"
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

diffdir=./diff
sqldir=./ddl
outdir=./output
blg=./blg-vulcan
# prefix=/net/txt/u/bioliv/VulcanJ/
prefix=./

# clean out old diff files, quietly
numTests=0
failedTests=0
mvs=false
verbose=false
start_tm=0
end_tm=0
time_each_test=false

echo "beginning fbmust tests..."

while [ -n "$(echo $1 | grep '-')" ]; do
   case $1 in
      -d ) sqldir="$2"
           shift
           ;;
      -o ) outdir="$2"
           shift
           ;;
      -v ) verbose='true'
           ;;
      -t ) time_each_test='true'
           ;;
      -m ) mvs='true'
           ;;
      *  ) echo 'usage: fbmust [-v] [-m] [-d directory] [-o directory]'
           echo 'example: fbmust -v -d ./ddl/nist'
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
