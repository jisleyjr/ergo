package org.ergoplatform.serialization

import org.ergoplatform.ErgoGenerators
import org.ergoplatform.modifiers.history.{ADProofSerializer, BlockTransactionsSerializer, HeaderSerializer}
import org.ergoplatform.modifiers.mempool.AnyoneCanSpendTransactionSerializer
import org.ergoplatform.modifiers.mempool.proposition.AnyoneCanSpendNoncedBoxSerializer
import org.ergoplatform.nodeView.history.ErgoSyncInfoSerializer
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}

class SerializationTests extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with ErgoGenerators with scorex.testkit.SerializationTests {


  property("AnyoneCanSpendBoxGen serialization") {
    checkSerializationRoundtrip(anyoneCanSpendBoxGen, AnyoneCanSpendNoncedBoxSerializer)
  }

  property("AnyoneCanSpendTransactionGen serialization") {
    checkSerializationRoundtrip(anyoneCanSpendTransactionGen, AnyoneCanSpendTransactionSerializer)
  }

  property("ErgoSyncInfo serialization") {
    checkSerializationRoundtrip(ergoSyncInfoGen, ErgoSyncInfoSerializer)
  }

  property("ErgoHeader serialization") {
    checkSerializationRoundtrip(ergoHeaderGen, HeaderSerializer)
  }

  property("BlockTransactions serialization") {
    checkSerializationRoundtrip(blockTransactionsGen, BlockTransactionsSerializer)
  }

  property("ADProofs serialization") {
    checkSerializationRoundtrip(ADProofsGen, ADProofSerializer)
  }

}
