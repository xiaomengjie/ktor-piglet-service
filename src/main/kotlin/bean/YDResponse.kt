package bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
* {
  "errorCode":"0",
  "query":"good", //查询正确时，一定存在
  "isDomainSupport":"true", //翻译结果是否为领域翻译(仅开通领域翻译时存在)
  "translation": [ //查询正确时一定存在
      "好"
  ],
  "basic":{ // 有道词典-基本词典,查词时才有
      "phonetic":"gʊd",
      "uk-phonetic":"gʊd", //英式音标
      "us-phonetic":"ɡʊd", //美式音标
      "uk-speech": "XXXX",//英式发音
      "us-speech": "XXXX",//美式发音
      "explains":[
          "好处",
          "好的",
          "好",
      ]
  },
  "web":[ // 有道词典-网络释义，该结果不一定存在
      {
          "key":"good",
          "value":["良好","善","美好"]
      },
      {...}
  ],
  "dict":{
      "url":"yddict://m.youdao.com/dict?le=eng&q=good"
  },
  "webdict":{
      "url":"http://m.youdao.com/dict?le=eng&q=good"
  },
  "l":"EN2zh-CHS",
  "tSpeakUrl":"XXX",//翻译后的发音地址
  "speakUrl": "XXX" //查询文本的发音地址
}
* */
@Serializable
data class YDResponse(
    @SerialName("basic")
    val basic: Basic = Basic(),
    @SerialName("dict")
    val dict: Dict = Dict(),
    @SerialName("errorCode")
    val errorCode: String = "",
    @SerialName("isDomainSupport")
    val isDomainSupport: Boolean= false,
    @SerialName("l")
    val l: String = "",
    @SerialName("query")
    val query: String = "",
    @SerialName("speakUrl")
    val speakUrl: String = "",
    @SerialName("tSpeakUrl")
    val tSpeakUrl: String = "",
    @SerialName("translation")
    val translation: List<String> = listOf(),
    @SerialName("web")
    val web: List<Web> = listOf(),
    @SerialName("webdict")
    val webdict: Webdict = Webdict()
)

@Serializable
data class Basic(
    @SerialName("explains")
    val explains: List<String> = listOf(),
    @SerialName("phonetic")
    val phonetic: String = "",
    @SerialName("uk-phonetic")
    val ukPhonetic: String = "",
    @SerialName("uk-speech")
    val ukSpeech: String = "",
    @SerialName("us-phonetic")
    val usPhonetic: String = "",
    @SerialName("us-speech")
    val usSpeech: String = ""
)

@Serializable
data class Dict(
    @SerialName("url")
    val url: String = ""
)

@Serializable
data class Web(
    @SerialName("key")
    val key: String = "",
    @SerialName("value")
    val value: List<String> = listOf()
)

@Serializable
data class Webdict(
    @SerialName("url")
    val url: String = ""
)