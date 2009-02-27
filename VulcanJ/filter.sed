s/Forced Writes are ON/./
s/Forced Writes are OFF/./
s/Number of DB pages allocated = [0-9]*. /./
s/Transaction - oldest = [0-9]/./
s/Transaction - oldest active = [0-9]/./
s/Transaction - oldest snapshot = [0-9]/./
s/Transaction - Next = [0-9]/./
s#/net/.*/input#.#
s#ddl/input#.#
# next line changes e+0nn to e+nn. this is to account for MS compiler that
# prints exponents with 0 padding, while other compilers don't.
# files are benched on unix system
s/\e+0\([0-9]*\)/e+\1/