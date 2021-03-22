package com.yeongil.focusaid.viewModel.viewModel.action

import androidx.lifecycle.*
import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.data.rule.action.AppBlockEntry
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.utils.ALERT
import com.yeongil.focusaid.utils.CLOSE_IMMEDIATE
import com.yeongil.focusaid.utils.Event
import com.yeongil.focusaid.utils.combineWith
import com.yeongil.focusaid.viewModel.item.AppBlockEntryItem
import com.yeongil.focusaid.viewModel.itemViewModel.AllAppBlockEntryItemViewModel
import com.yeongil.focusaid.viewModel.itemViewModel.AppBlockEntryItemViewModel
import com.yeongil.focusaid.viewModel.itemViewModel.HelpPhraseItemViewModel

class AppBlockActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    /* Data */
    var originalAction: AppBlockAction? = null
    val appBlockEntryList = MutableLiveData<List<AppBlockEntry>>()
    val allAppBlock = MutableLiveData<Boolean>(false)
    val allAppHandlingAction = MutableLiveData<Int>(CLOSE_IMMEDIATE)

    // Indicate whether the user have clicked any item. It's for showing help
    val isFirstItemClick = MutableLiveData<Boolean>(true)

    /* View Related */
    // Recycler Item List
    val allAppBlockEntryItemList = allAppHandlingAction
        .combineWith(isFirstItemClick) { a, b -> Pair(a, b) }
        .map { (handlingAction, isFirst) ->
            val description = when (handlingAction) {
                CLOSE_IMMEDIATE -> "바로 종료"
                ALERT -> "경고 알림"
                else -> ""
            }

            listOf(
                AllAppBlockEntryItemViewModel(
                    description = description,
                    isFirstClick = isFirst ?: true,
                    onClickItem = onAllAppItemClick,
                    onClickDeleteItem = onAllAppItemDelete
                ).toRecyclerItem(),
                HelpPhraseItemViewModel(
                    "카드 버튼을 터치하면 모든 앱 \n" +
                            "사용을 제한하거나 앱 사용시 \n" +
                            "경고가 표시되도록 설정할 수 \n" +
                            "있습니다 (전화 및 시스템 앱은 \n" +
                            "제외) "
                ).toRecyclerItem()
            )
        }
    val appBlockEntryItemList = appBlockEntryList
        .combineWith(isFirstItemClick) { a, b -> Pair(a, b) }
        .map { (entryList, isFirst) ->
            val list = entryList ?: listOf()

            if (list.isEmpty()) {
                listOf(
                    HelpPhraseItemViewModel(
                        "제한 앱 추가 버튼을 터치하여 \n" +
                                "제한할 앱을 추가해 주세요 \n" +
                                "(동시에 여러 앱 선택 가능) "
                    ).toRecyclerItem()
                )
            } else {
                list.map {
                    val item = AppBlockEntryItem(it, pmRepo)
                    val itemViewModel = AppBlockEntryItemViewModel(
                        item.packageName,
                        item,
                        isFirst ?: true,
                        onClickItem,
                        onClickItemDelete
                    )

                    itemViewModel.toRecyclerItem()
                } + HelpPhraseItemViewModel(
                    "카드 버튼을 터치하여 앱 사용 허용 \n" +
                            "시간을 추가로 설정할 수 있습니다 \n" +
                            "\n" +
                            "허용 시간 초과시 앱 사용을 \n" +
                            "제한하거나 앱 사용시 경고가 \n" +
                            "표시되도록 설정할 수 있습니다 "
                ).toRecyclerItem()
            }
        }

    // Action Exists; for blocking complete button
    val actionExists = appBlockEntryList
        .combineWith(allAppBlock) { list, bool -> Pair(list, bool) }
        .map { (list, allAppBlock) -> list?.isNotEmpty() ?: false || allAppBlock ?: false }

    // item click
    val itemClickEvent = MutableLiveData<Event<AppBlockEntry>>()
    private val onClickItem: (String) -> Unit = { packageName ->
        isFirstItemClick.value = false
        itemClickEvent.value = Event(getAppBlockEntry(packageName))
    }

    // item delete click
    private val onClickItemDelete: (String) -> Unit = { packageName ->
        val oldEntryList = appBlockEntryList.value ?: listOf()
        val index = oldEntryList.map { it.packageName }.indexOf(packageName)
        appBlockEntryList.value = oldEntryList - oldEntryList[index]
    }

    // all app item click
    val allAppItemClickEvent = MutableLiveData<Event<Int>>()
    private val onAllAppItemClick: () -> Unit = {
        isFirstItemClick.value = false
        allAppItemClickEvent.value = Event(getAllAppHandlingAction())
    }

    // all app item delete
    private val onAllAppItemDelete: () -> Unit = { allAppBlock.value = false }

    ///////////////
    /* Rule Edit */
    ///////////////
    fun getAppBlockAction(): AppBlockAction? {
        val list = appBlockEntryList.value ?: listOf()
        val allApp = allAppBlock.value ?: false
        val handlingAction = allAppHandlingAction.value!!

        return if (list.isEmpty() && !allApp) null
        else AppBlockAction(list, allApp, handlingAction)
    }

    fun putAppBlockAction(action: AppBlockAction?) {
        originalAction = action
        allAppBlock.value = action?.allAppBlock ?: false
        allAppHandlingAction.value = action?.allAppHandlingAction ?: CLOSE_IMMEDIATE
        appBlockEntryList.value = action?.appBlockEntryList ?: listOf()

        isFirstItemClick.value = action == null
    }

    //////////////
    /* App List */
    //////////////
    fun getAppList(): List<String>? {
        return if (allAppBlock.value == true) null
        else appBlockEntryList.value?.map { it.packageName } ?: listOf()
    }

    fun updateAppBlockEntryList(appList: List<String>) {
        val oldList = appBlockEntryList.value ?: listOf()
        val subtractedList = oldList.filter { appList.contains(it.packageName) }
        val subtractedAppList = subtractedList.map { it.packageName }
        val additionList = appList
            .filterNot { subtractedAppList.contains(it) }
            .map { AppBlockEntry(it, 0, CLOSE_IMMEDIATE) }

        appBlockEntryList.value = subtractedList + additionList
        allAppBlock.value = false
    }

    fun putAllApp() {
        appBlockEntryList.value = listOf()
        allAppBlock.value = true
        allAppHandlingAction.value = CLOSE_IMMEDIATE
    }

    /////////////////////
    /* App Block Entry */
    /////////////////////
    fun updateAllAppHandlingAction(handlingAction: Int) {
        allAppHandlingAction.value = handlingAction
    }

    private fun getAllAppHandlingAction(): Int {
        return allAppHandlingAction.value ?: CLOSE_IMMEDIATE
    }

    private fun getAppBlockEntry(packageName: String): AppBlockEntry {
        return appBlockEntryList.value!!.last { it.packageName == packageName }
    }

    fun updateAppBlockEntryList(entry: AppBlockEntry) {
        with(appBlockEntryList) {
            val index = value!!.indexOfFirst { it.packageName == entry.packageName }
            value = value!!.toMutableList().apply { this[index] = entry }
        }
    }
}