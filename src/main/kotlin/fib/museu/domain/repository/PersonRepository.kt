package fib.museu.domain.repository

import fib.museu.domain.datamodels.GuideObject
import fib.museu.domain.datamodels.VisitorObject

interface PersonRepository {
    fun getVisitor(email: String): VisitorObject
    fun getGuide(email: String): GuideObject
    fun setVisitor(visitor: VisitorObject)
    fun setGuide(guide: GuideObject)
}