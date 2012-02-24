

package fureteur.config

import scala.collection.mutable._

import com.rabbitmq.client._
import fureteur.data._

class Config(d:Data) {
  val data= d

  def apply(s:String):String = {
    data get s
  }

  def getInt(s:String) = {
    (data get s).toInt
  }

  def getOption(s:String) = {
	data getOption s
  }

  def getLongOption(s:String) = {
	(data getOption s) match { 
      case Some(s) => Some(s.toLong) 
      case _ => None
	}
  }

  def getObject(s:String):Config = { new Config(data.getObject(s)) }

  def unwrapArray(s:String):List[Config] = {
	(data unwrapArray s) map (new Config(_))
  }

}

object Config {

  val configs = new HashMap[String, (String, Config)]()

  def registerConfig(s:String):Unit = {
	val c= Config.fromJson(s)
    configs+= (c("conf") -> (s,c))
  }

  def getConfig(s:String) = {
    configs(s)._2
  }

  def dumpConfig(s:String) = {
    configs(s)._1	
  }

  def fromJson(s:String) = {
	new Config( Data.fromJson(s) )
  }

  def showConfigs() ={
	configs.toList.map ( kkv => (kkv._1,kkv._2._2("description") ) )
  }

}