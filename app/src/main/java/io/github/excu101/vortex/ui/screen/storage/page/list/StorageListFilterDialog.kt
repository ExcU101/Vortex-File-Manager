package io.github.excu101.vortex.ui.screen.storage.page.list

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.provider.storage.Filter
import io.github.excu101.vortex.provider.storage.Order
import io.github.excu101.vortex.provider.storage.Sorter
import io.github.excu101.vortex.provider.storage.View
import io.github.excu101.vortex.ui.component.storage.filter.StorageListFilterBinding

class StorageListFilterDialog(
    context: Context,
    private val onViewChange: (View) -> Unit,
    private val onSortChange: (Sorter<PathItem>) -> Unit,
    private val onOrderChange: (Order) -> Unit,
    private val onFilterChange: (Filter<PathItem>) -> Unit
) : BottomSheetDialog(context) {

    private val binding = StorageListFilterBinding(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.groups[0].addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                ViewIds.Storage.Sort.ColumnId -> {
                    if (isChecked) {
                        onViewChange(View.COLUMN)
                    }
                }

                ViewIds.Storage.Sort.GridId -> {
                    if (isChecked) {
                        onViewChange(View.GRID)
                    }
                }
            }
        }

        binding.groups[1].addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                ViewIds.Storage.Sort.AscendingId -> {
                    if (isChecked) {
                        onOrderChange(Order.ASCENDING)
                    }
                }

                ViewIds.Storage.Sort.DescendingId -> {
                    if (isChecked) {
                        onOrderChange(Order.DESCENDING)
                    }
                }
            }
        }

        binding.groups[2].addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                ViewIds.Storage.Sort.NameId -> {
                    if (isChecked) {
                        onSortChange(PathItemSorters.Name)
                    }
                }

                ViewIds.Storage.Sort.PathId -> {
                    if (isChecked) {
                        onSortChange(PathItemSorters.Path)
                    }
                }

                ViewIds.Storage.Sort.SizeId -> {
                    if (isChecked) {
                        onSortChange(PathItemSorters.Size)
                    }
                }
            }
        }

        binding.groups[3].addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                ViewIds.Storage.Sort.OnlyFoldersId -> {
                    onFilterChange(PathItemFilters.OnlyFolder)
                }

                ViewIds.Storage.Sort.OnlyFilesId -> {
                    onFilterChange(PathItemFilters.OnlyFile)
                }

                else -> {
                    onFilterChange(PathItemFilters.Empty)
                }
            }
        }
    }

}