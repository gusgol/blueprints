package tech.hiregus.trendingprojects.di

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class InjectorInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication {
        return startKoin {
            androidContext(context)
            modules(
                listOf(
                    projectsModule
                )
            )
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}