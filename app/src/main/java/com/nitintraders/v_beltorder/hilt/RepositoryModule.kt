package com.nitintraders.v_beltorder.hilt

import com.nitintraders.v_beltorder.repository.BeltOrdersRepository
import com.nitintraders.v_beltorder.repository.BeltOrdersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule() {

    @Binds
    abstract fun bindBeltOrdersRepository(
        beltOrdersRepositoryImpl: BeltOrdersRepositoryImpl
    ): BeltOrdersRepository

}
