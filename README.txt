The program sets mp3 id tags for mp3 files, based on folder structure.

compile:
scalac -deprecation -cp /path/to/jaudiotagger-2.0.4-20111207.115108-15.jar mp3dirtag.scala

run:
scala -cp /path/to/jaudiotagger-2.0.4-20111207.115108-15.jar:. mp3 -artist Artist -genre Classical /ath/to/mp3/files

