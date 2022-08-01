package com.example.shopperista.activities.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.AddProductActivity
import com.example.shopperista.adapters.MyProductsListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Product
import com.example.shopperista.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    private fun getProductListFromFirestore(){
        showProgressDialog()
        FirestoreClass().getProductsList(this@ProductsFragment)
    }

    fun successProductsListFromFirestore(productsList:ArrayList<Product>){
        hideProgressDialog()

        if(productsList.size>0){
            rv_my_product_items.visibility=View.VISIBLE
            tv_no_products_found.visibility=View.GONE

            rv_my_product_items.layoutManager=LinearLayoutManager(activity)

            val adapterProducts = MyProductsListAdapter(requireActivity(),productsList,this@ProductsFragment)
            rv_my_product_items.adapter=adapterProducts
        }
        else{
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.VISIBLE
        }
    }

    fun deleteProduct(ProductID : String){
        showAlertDialogToDeleteProduct(ProductID)
    }

    private fun showAlertDialogToDeleteProduct(productID : String){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete ")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            showProgressDialog()
            FirestoreClass().deleteProduct(this@ProductsFragment, productID)

            dialogInterface.dismiss()
        }


        builder.setNegativeButton("No") { dialogInterface, _ ->

            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun productDeleteSuccessMethod(){
        hideProgressDialog()
        Toast.makeText(requireActivity(),"Deleted",Toast.LENGTH_SHORT).show()
        getProductListFromFirestore()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()

        getProductListFromFirestore()
    }

}