package scorex.testkit.generators

import org.ergoplatform.modifiers.ErgoPersistentModifier
import org.ergoplatform.nodeView.history.ErgoHistory
import org.ergoplatform.nodeView.state.ErgoState

sealed trait ModifierProducerTemplateItem

case object SynInvalid extends ModifierProducerTemplateItem
case object Valid extends ModifierProducerTemplateItem

trait CustomModifierProducer[ST <: ErgoState[ST]] {

  def customModifiers(history: ErgoHistory,
                      state: ST,
                      template: Seq[ModifierProducerTemplateItem]): Seq[ErgoPersistentModifier]
}
