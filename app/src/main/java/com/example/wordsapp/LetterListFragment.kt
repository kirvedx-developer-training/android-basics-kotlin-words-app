package com.example.wordsapp

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.FragmentLetterListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LetterListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LetterListFragment : Fragment() {
    // TODO: As in any fragment we need properties for:
    //
    // 1. A reference to the binding that's generated for this fragment.
    //    The ? makes its type nullable, because until onCreateView there will
    //    be no such binding:
    private var _binding: FragmentLetterListBinding? = null

    // 2. So that we don't litter the code with ?'s (nullable typesetter), let's
    //    reference the binding again, assigning the original nullable reference
    //    with !!'s appended - which signifies we know it has a value (at this point)
    //    so no need for null-safety - to a get-only [hence, get() on the left hand
    //    side of our assignment] value; which means that the value cannot be assigned
    //    elsewhere once its assigned somewhere (as it is here):
    private val binding get() = _binding!!

    // 3. Let's create a property for the recycler view. We need this here because we
    //    simply call setHasOptionsMenu in onCreate, inflating the layout(s), setting out
    //    binding and returning the view as the binding's root in onCreateView. We'll set
    //   The recyclerView in onViewCreated - so we'll need this scoped to do that just
    //   like we have binding scoped similarly:
    //
    private lateinit var recyclerView: RecyclerView

    // 4. Of course, let's migrate the following over from the activity form of this
    //    implementation - as it IS an implementation device:
    private var isLinearLayoutManager = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Notice that the binding already created a property for our recyclerView and how
        // we do not need to call findViewById to get a reference to it:
        recyclerView = binding.recyclerView

        chooseLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Manually set the _binding's value to null as we could create and destroy our layout
        // (visible artifacts) several times through out a fragment's lifecycle, and we need to
        // know when it is null or set to a value (because the layout either isnt visible yet, or
        // because it's just been created - so we have to know when its been destroyed in THIS
        // fashion):
        _binding = null
    }

    // The onCreateOptionsMenu method slightly differs from an activity's in that there is no
    // global menuInflator - instead it is passed in to the method itself. Also, no return is
    // necessary here:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    private fun chooseLayout() {
        when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = LetterAdapter()
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = LetterAdapter()
            }
        }
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
                if (isLinearLayoutManager)
                    ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
                else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}