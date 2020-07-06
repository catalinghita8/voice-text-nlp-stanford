package com.example.arvoice.nlp

import com.example.arvoice.domain.LogItem
import com.example.arvoice.logs.LogsHelper
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import kotlinx.coroutines.*
import java.util.*


class NLPClient(private val pipeline: StanfordCoreNLP) {

    fun extractRootNoun(text: CharSequence): Pair<String?, List<LogItem>> {
        val input = text.toString()
        val currentTime = Calendar.getInstance().timeInMillis
        val logs = arrayListOf<LogItem>()

        val document = Annotation(input)

        pipeline.annotate(document)
        val sentences: List<CoreMap> =
            document.get(CoreAnnotations.SentencesAnnotation::class.java)
        for (sentence in sentences) {
            val tokens = sentence.get(CoreAnnotations.TokensAnnotation::class.java)
            logs.add(LogsHelper.createLog("Identified ${tokens.size} words"))

            for (token in tokens) { // this is the text of the token
                val word = token.get(CoreAnnotations.TextAnnotation::class.java)
                // POS tag of the token
                val pos = token.get(CoreAnnotations.PartOfSpeechAnnotation::class.java)

                //  NER label of the token
                if(pos.toString() == "NN" || pos.toString() == "NNS") {
                    logs.add(LogsHelper.createLog("Identified  $word with POS: $pos"))
                    val lemmatizedValue = token.lemma().toString()
                    logs.add(LogsHelper.createLog("Singular form : ${token.lemma()}"))
                    val difference = Calendar.getInstance().timeInMillis - currentTime
                    logs.add(LogsHelper.createLog( "NLP tasks took: $difference ms"))

                    return Pair(lemmatizedValue, logs)
                }
            }
        }

        return Pair(null, logs)
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: NLPClient? = null

        suspend fun getInstance() = withContext(Dispatchers.IO) {
            instance ?: synchronized(this) {
                val props = Properties()
                props.setProperty("annotators", "tokenize, ssplit, pos, lemma")
                instance
                    ?: NLPClient(StanfordCoreNLP(props)).also { instance = it }
            }
        }
    }

}