package tk.atna.audiorecorder.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tk.atna.audiorecorder.data.repository.FileCacheRepositoryImpl
import tk.atna.audiorecorder.data.repository.RecordingRepositoryImpl
import tk.atna.audiorecorder.domain.interactor.RecordsInteractor
import tk.atna.audiorecorder.domain.repository.FileCacheRepository
import tk.atna.audiorecorder.domain.repository.RecordingRepository
import tk.atna.audiorecorder.presentation.main.MainViewModel

val appModule = module {

    single<FileCacheRepository> { FileCacheRepositoryImpl(get()) }
    single<RecordingRepository> { RecordingRepositoryImpl() }

    factory { RecordsInteractor(get(), get()) }

    viewModel { MainViewModel(get()) }
}
