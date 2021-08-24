package com.example.emptyviewbinding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.emptyviewbinding.data.NbaPlayer
import com.example.emptyviewbinding.databinding.MainFragmentBinding
import com.example.emptyviewbinding.preference.PreferenceActivity


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val mBinding get() = _binding!!
    lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: ItemsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        FITLER = prefs.getString("prefFilter", "").toString()
        Log.i("123", "FILTER:${FITLER}")
        mAdapter = ItemsAdapter()
        mBinding.recycler.adapter = mAdapter
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mViewModel.readAllData.observe(viewLifecycleOwner, Observer { list ->
            mAdapter.submitList(list)

        })
        mBinding.fabAdd.setOnClickListener {
            mViewModel.insert(NbaPlayer(0, getRandomString(3), (1..12).shuffled().first(), getRandomColor()))

        }
        return mBinding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
    private fun getRandomColor() : String {
        val black ="Black"
        val white ="White"
        val list = mutableListOf<String>(black ,white)
        return list.shuffled().first()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.main_menu){
            val intent = Intent(requireActivity(), PreferenceActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
            mViewModel.readAllData.removeObservers(viewLifecycleOwner)
        mBinding.recycler.adapter = null
        _binding = null
    }


}

