package com.nitintraders.v_beltorder.hilt

import android.content.Context
import com.nitintraders.v_beltorder.database.BeltOrderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideBeltOrderDatabase(
        @ApplicationContext applicationContext: Context
    ): BeltOrderDatabase = BeltOrderDatabase.getInstance(applicationContext)

}
