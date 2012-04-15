This tool sets mp3 id tags for mp3 files, based on folder structure.

compile:
scalac -deprecation -cp /path/to/jaudiotagger-2.0.4-20111207.115108-15.jar mp3dirtag.scala

examples:
1. Simple direcory structure:

/Bach Johann Sebastian/Motetten
   01-.....mp3
   02-.....mp3
   ...

scala -cp /path/to/jaudiotagger-2.0.4-20111207.115108-15.jar:. mp3 -artist "My favorite Artist" -genre Classical /Bach/Motetten

1. Deep direcory structure:

/Bach Johann Sebastian/Brandenburgische Konzerte/Konzert 1
   01-...mp3
   02-...mp3
   ...
/Bach Johann Sebastian/Brandenburgische Konzerte/Konzert 2
   01-...mp3
   02-...mp3
   ...

scala -cp /path/to/jaudiotagger-2.0.4-20111207.115108-15.jar:. mp3 -artist "My favorite Artist" -genre Classical -number-consecutively -level 1 /Bach Johann Sebastian/Brandenburgische Konzerte

