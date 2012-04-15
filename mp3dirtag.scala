import org.jaudiotagger.audio._
import org.jaudiotagger.tag._
import java.io.File
import java.util.HashMap
import scala.collection.JavaConversions._

object mp3 {
  type OptionMap = Map[Symbol,String]

  def getFiles(file: File): List[AudioFile] = {
    file match {
      case f: File if f.isFile && (f.getName.endsWith(".mp3") || f.getName.endsWith("m4a")) => List(AudioFileIO.read(file))
      case f: File if f.isDirectory => f.listFiles.toList.map(f => getFiles(f)).flatten
      case _ => Nil
    }
  }

  def getParentFile(file: File, level: Int): File = {
    level match {
      case n: Int if 0 < n => getParentFile(file.getParentFile, n-1)
      case _ => file.getParentFile
    }
  }

  def writeTag(audioFile: AudioFile, options: OptionMap, track: Int, level: Int) {
    val file = audioFile.getFile
    val fileName = file.getName
    val title = fileName.substring(0, fileName.length - 4)
    val parentFile = getParentFile(file, level)
    val composer = parentFile.getParentFile.getName
    val album = composer.split(" ")(0)+" - "+parentFile.getName
    val artist = options.getOrElse('artist, "-")
    val genre = options.getOrElse('genre, "-")
    
    println("title: "+title+", album: "+album+" composer: "+composer)
    val tag: Tag = audioFile.getTagOrCreateAndSetDefault
    tag.setField(FieldKey.TITLE,title)
    tag.setField(FieldKey.ALBUM,album)
    tag.setField(FieldKey.COMPOSER,composer)
    if (track != 0) {
      tag.setField(FieldKey.TRACK,track.toString)
    }
    else {
      tag.setField(FieldKey.TRACK,title.substring(0,2))
     }
    if (genre != "-") {
      tag.setField(FieldKey.GENRE,genre)
    }
    if (artist != "-") {
      tag.setField(FieldKey.ARTIST,artist)
    }
    audioFile.commit()
  }

  def main(args: Array[String]) {
    val usage = """
    Usage: mp3 [-artist value] [-genre value] directory
    """  
    if (args.length == 0) println(usage)
    
    def nextOption(map : OptionMap, list: List[String]) : OptionMap = {
      def isSwitch(s : String) = (s(0) == '-')
	list match {
          case Nil => map
          case "-genre" :: value :: tail => nextOption(map ++ Map('genre -> value), tail)
          case "-artist" :: value :: tail => nextOption(map ++ Map('artist -> value), tail)
          case "-level" :: value :: tail => nextOption(map ++ Map('level -> value), tail)
          case string :: Nil =>  nextOption(map ++ Map('directory -> string), list.tail)
          case option :: tail => println("Unknown option '" + option + "'\n" + usage) 
                                 sys.exit(1) 
	}
    }
    val options = nextOption(Map(),args.toList)

    val files = getFiles(new File(options('directory)))
    println("Settings tags in "+files.size+" files in directory "+options('directory))
    files.foreach(x => writeTag(x, options, 0, 0))
  }
}
