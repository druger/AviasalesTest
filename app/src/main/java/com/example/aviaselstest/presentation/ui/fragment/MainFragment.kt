package com.example.aviaselstest.presentation.ui.fragment

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.aviaselstest.R
import com.example.aviaselstest.domain.model.City
import com.example.aviaselstest.presentation.model.Direction
import com.example.aviaselstest.presentation.viewmodel.MainViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFind.setOnClickListener { openRouteInfo() }
        etFrom.addTextChangedListener {
            fromField.error = null
            getCities(it, Direction.FROM)
        }
        etTo.addTextChangedListener {
            toField.error = null
            getCities(it, Direction.TO)
        }
        observeCities()
    }

    private fun getCities(editable: Editable?, direction: Direction) {
        viewModel.getCities(editable.toString().trim(), direction)
    }

    private fun observeCities() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            when (it.second) {
                Direction.FROM -> showCites(it.first, etFrom, it.second)
                Direction.TO -> showCites(it.first, etTo, it.second)
            }
        })
    }

    private fun showCites(cities: List<City>?, view: AutoCompleteTextView, direction: Direction) {
        cities?.let { list ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list
            )
            view.setAdapter(adapter)
            view.setOnItemClickListener { parent, _, position, _ ->
                val city = parent.getItemAtPosition(position) as City
                when (direction) {
                    Direction.FROM -> viewModel.from = city
                    Direction.TO -> viewModel.to = city
                }
            }
        }
    }

    private fun openRouteInfo() {
        if (!isEmptyFields()) {
            val routeFragment = RouteFragment.newInstance()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, routeFragment)
                .addToBackStack(null)
                .commit()
        } else {
            setFieldsError()
        }
    }

    private fun setFieldsError() {
        if (isEmptyFrom()) setError(fromField)
        if (isEmptyTo()) setError(toField)
    }

    private fun setError(input: TextInputLayout) {
        input.error = getString(R.string.empty_filed_error)
    }

    private fun isEmptyFields() = isEmptyFrom() || isEmptyTo()

    private fun isEmptyTo() = etTo.text.toString().trim().isEmpty()

    private fun isEmptyFrom() = etFrom.text.toString().trim().isEmpty()

    companion object {
        fun newInstance() = MainFragment()
    }
}
