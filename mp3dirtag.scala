import org.jaudiotagger.audio._
import org.jaudiotagger.tag._
import java.io.File
import scala.collection.JavaConversions._

object mp3 {
  def getFiles(file: File): List[AudioFile] = {
    file match {
      case f: File if f.isFile && (f.getName.endsWith(".mp3") || f.getName.endsWith("m4a")) => List(AudioFileIO.read(file))
      case f: File if f.isDirectory => f.listFiles.toList.map(f => getFiles(f)).flatten
      case _ => Nil
    }
  }

  def writeTag(audioFile: AudioFile, genre: String, artist: String) {
    val file = audioFile.getFile
    val fileName = file.getName
    val title = fileName.substring(0, fileName.length - 4)
    val composer = file.getParentFile.getParentFile.getName
    val album = composer.split(" ")(0)+" - "+file.getParentFile.getName
    
    println("title: "+title+", album: "+album+", composer: "+composer+", genre: "+genre+", artist: "+artist)
    val tag: Tag = audioFile.getTagOrCreateAndSetDefault
    tag.setField(FieldKey.TITLE,title)
    tag.setField(FieldKey.ALBUM,album)
    tag.setField(FieldKey.COMPOSER,composer)
    tag.setField(FieldKey.GENRE,genre)
    tag.setField(FieldKey.TRACK,title.substring(0,2))
    if (artist !=null && artist != "") {
      tag.setField(FieldKey.ARTIST,artist)
    }
    audioFile.commit()
  }

  def main(args: Array[String]) {
    val files = getFiles(new File(args(0)))
    files.foreach(x => writeTag(x, args(1), args(2)))
  }
}
