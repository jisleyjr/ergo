package org.ergoplatform.modifiers.state

import org.ergoplatform.ErgoBox
import org.ergoplatform.modifiers.ErgoPersistentModifier
import org.ergoplatform.modifiers.state.UTXOSnapshotChunk.StateElement
import org.ergoplatform.settings.Algos
import scorex.core.ModifierTypeId
import scorex.core.serialization.ScorexSerializer
import scorex.crypto.authds.LeafData
import scorex.crypto.hash.Digest32
import scorex.util.{ModifierId, bytesToId}
import scorex.utils.Random

case class UTXOSnapshotChunk(stateElements: Seq[StateElement],
                             index: Short) extends ErgoPersistentModifier {
  override val modifierTypeId: ModifierTypeId = UTXOSnapshotChunk.modifierTypeId

  //TODO implement correctly
  override lazy val id: ModifierId = bytesToId(Random.randomBytes(32))

  override def parentId: ModifierId = ???

  override def serializedId: Array[Byte] = ???

  override type M = UTXOSnapshotChunk

  override def serializer: ScorexSerializer[UTXOSnapshotChunk] = ???

  lazy val rootHash: Digest32 = Algos.merkleTreeRoot(stateElements.map(LeafData @@ _.bytes))

  override val sizeOpt: Option[Int] = None

}

object UTXOSnapshotChunk {
  type StateElement = ErgoBox

  val modifierTypeId: ModifierTypeId = ModifierTypeId @@ (107: Byte)
}
