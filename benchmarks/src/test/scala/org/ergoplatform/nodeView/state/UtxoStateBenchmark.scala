package org.ergoplatform.nodeView.state

import org.ergoplatform.Utils
import org.ergoplatform.Utils.BenchReport
import org.ergoplatform.modifiers.ErgoPersistentModifier
import org.ergoplatform.nodeView.NVBenchmark
import org.ergoplatform.settings.{Args, ErgoSettings}
import org.ergoplatform.utils.HistoryTestHelpers

object UtxoStateBenchmark extends HistoryTestHelpers with NVBenchmark {

  val WarmupRuns = 2

  def main(args: Array[String]): Unit = {

    val startTs = System.currentTimeMillis()

    val realNetworkSetting = ErgoSettings.read(Args(Some("src/main/resources/application.conf"), None))

    val blocks = readBlocks

    val transactionsQty = blocks.flatMap(_.transactions).size

    def bench(mods: Seq[ErgoPersistentModifier]): Long = {
      val state = ErgoState.generateGenesisUtxoState(createTempDir, StateConstants(realNetworkSetting), parameters)._1
      Utils.time {
        mods.foldLeft(state) { case (st, mod) =>
          st.applyModifier(mod)(_ => ()).get
        }
      }.toLong
    }

    (0 to WarmupRuns).foreach(_ => bench(blocks))

    val et = bench(blocks)

    println(s"Performance of `$transactionsQty transactions application`: $et ms")

    Utils.dumpToFile("UtxoStateBenchmark", startTs, Seq(BenchReport(s"$transactionsQty transactions application", et)))

    System.exit(0)

  }

}
