package com.hyoseok.service

import com.hyoseok.product.repository.ExternalProductRepository
import com.hyoseok.product.repository.read.ExternalProductReadRepository
import com.hyoseok.service.dto.ProductCreateDto
import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.SnsRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SnsService(
    private val snsRepository: SnsRepository,
    private val snsReadRepository: SnsReadRepository,
    private val externalProductRepository: ExternalProductRepository,
    private val externalProductReadRepository: ExternalProductReadRepository,
) {

    @Transactional
    fun create(dto: SnsCreateDto): SnsCreateResultDto {
        val sns: Sns = dto.toEntity()
        snsRepository.save(sns = sns)
        externalProductRepository.saveAll(
            externalProducts = ProductCreateDto.toEntities(
                products = dto.products,
                snsId = sns.id!!,
                memberId = sns.memberId,
            ),
        )
        return SnsCreateResultDto(sns = sns)
    }

    fun findWithAssociatedEntitiesById(snsId: Long): SnsFindResultDto = runBlocking {
        val asyncSns = async(context = Dispatchers.IO) {
            snsReadRepository.findWithAssociatedEntitiesById(snsId = snsId)
        }
        val asyncExternalProducts = async(context = Dispatchers.IO) {
            externalProductReadRepository.findAllBySnsId(snsId = snsId)
        }
        SnsFindResultDto(sns = asyncSns.await(), externalProducts = asyncExternalProducts.await())
    }
}
