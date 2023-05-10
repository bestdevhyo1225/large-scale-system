package com.hyoseok.base.repository

import com.hyoseok.base.entity.Child
import com.hyoseok.base.entity.Parent
import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        ParentRepository::class,
    ],
)
internal class ParentRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var parentRepository: ParentRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    init {
        this.afterSpec {
            withContext(Dispatchers.IO) {
                parentRepository.deleteAll()
            }
        }

        this.describe("복합키 테스트") {
            it("@IdClass 테스트") {
                val child1 = Child(id = 1L)
                val child2 = Child(id = 2L)
                val parent = Parent(name = "parent")
                parent.addChild(child = child1)
                parent.addChild(child = child2)

                parentRepository.save(parent)

                entityManager.flush()
                entityManager.clear()

                val findParent: Parent = parentRepository.findByIdOrNull(parent.id!!)!!
                val findChilds: List<Child> = findParent.childs

                findChilds.forEach {
                    println("child.id = ${it.id}")
                    println("child.parent.id = ${it.parent!!.id}")
                }
            }
        }
    }
}
