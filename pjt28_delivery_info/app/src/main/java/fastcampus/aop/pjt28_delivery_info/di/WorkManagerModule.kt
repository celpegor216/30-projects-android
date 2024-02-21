package fastcampus.aop.pjt28_delivery_info.di

import fastcampus.aop.pjt28_delivery_info.work.TrackingCheckWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workManagerModule = module {
    worker { TrackingCheckWorker(get(), get()) }
}