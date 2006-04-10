s/Forced Writes are ON/./
s/Forced Writes are OFF/./
s/ -2.488355210669293e+39/./
s/-2.488355210669293e+039/./
s/Number of DB pages allocated = [0-9]*. /./
s/Transaction - oldest = [0-9]/./
s/Transaction - oldest active = [0-9]/./
s/Transaction - oldest snapshot = [0-9]/./
s/Transaction - Next = [0-9]/./
s#/net/.*/input#.#
s#ddl/input#.#