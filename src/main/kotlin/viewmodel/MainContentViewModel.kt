package viewmodel

import com.jthemedetecor.OsThemeDetector
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import model.entity.Message
import model.usecase.GetMessageFlowUseCase
import model.usecase.IsSetupCompletedUseCase
import resource.Strings.NOT_FOUND_ADB_COMMAND
import resource.Strings.NOT_FOUND_SCRCPY_COMMAND
import view.page.Page


class MainContentViewModel(
    private val isSetupCompletedUseCase: IsSetupCompletedUseCase,
    private val getMessageFlowUseCase: GetMessageFlowUseCase
) : ViewModel() {

    private val _selectedPages: MutableStateFlow<Page> = MutableStateFlow(Page.DevicesPage)
    val selectedPages: StateFlow<Page> = _selectedPages

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _notifyMessage: MutableStateFlow<Message> = MutableStateFlow(Message.EmptyMessage)
    val notifyMessage: StateFlow<Message> = _notifyMessage

    private val detector = OsThemeDetector.getDetector()
    private val darkThemeListener: (Boolean) -> Unit = { isDark: Boolean ->
        coroutineScope.launch { _isDarkMode.emit(isDark) }
    }
    private val _isDarkMode: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    override fun onStarted() {
        checkError()
        observeNotifyMessage()
        detector.registerListener(darkThemeListener)
    }

    override fun onCleared() {
        detector.removeListener(darkThemeListener)
    }

    fun selectPage(page: Page) {
        _selectedPages.value = page
    }

    fun checkError() {
        coroutineScope.launch {
            _errorMessage.value = when (isSetupCompletedUseCase.execute()) {
                IsSetupCompletedUseCase.Result.NOT_FOUND_SCRCPY_COMMAND -> NOT_FOUND_SCRCPY_COMMAND
                IsSetupCompletedUseCase.Result.NOT_FOUND_ADB_COMMAND -> NOT_FOUND_ADB_COMMAND
                else -> null
            }
        }
    }

    private fun observeNotifyMessage() {
        coroutineScope.launch {
            getMessageFlowUseCase.execute().collect {
                _notifyMessage.value = it
                delay(2000)
                _notifyMessage.value = Message.EmptyMessage
            }
        }
    }
}
