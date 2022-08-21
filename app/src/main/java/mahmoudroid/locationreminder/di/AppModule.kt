package mahmoudroid.locationreminder.di

import android.content.Context
import android.content.SharedPreferences
import at.favre.lib.armadillo.Armadillo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mahmoudroid.locationreminder.data.source.SharedPref
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context = appContext

    @Singleton
    @Provides
    fun provideSharedPref(sharedPreferences: SharedPreferences) = SharedPref(sharedPreferences)

    @Singleton
    @Provides
    fun provideSharedPreferences(appContext: Context): SharedPreferences {
        return Armadillo.create(appContext, "locationReminder").encryptionFingerprint(appContext).build()
    }

}