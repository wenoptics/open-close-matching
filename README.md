# Open-Close Matching

## Assignment Desc.

Write a program that matches opening and ending symbols in a 
java program. `()`, `{}`, `[]`, `“”` (double quotes), and `‘’`
(single quotes) to insure that the last opening symbol 
is followed by the first closing symbol.

That is `(…[….]….)` is okay, but `(….[….)…..]` is not okay.

Ignore opening and closing symbols within comments (`//` to end 
of line and inside `/*` to `*/` blocks) and opening and closing symbols 
immediately following a \ (back-slash character).  `/*` and `*/` comments 
do NOT stack one `*/` closes any number of `/*` open comment markers.

Test your program by running it with a java program that has all five 
opening and closing symbols and has at least one symbol in a comment 
and a different symbol following a backslash.

You will have to decide how you want to treat set of quotes (double 
and single) -- do you want to check what is inside of them for 
opening and closings or treat what is inside the opening and 
closing quotes as comments that can be ignored.  Either way is okay.

