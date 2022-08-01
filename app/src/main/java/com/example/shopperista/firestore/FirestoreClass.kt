package com.example.shopperista.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.shopperista.activities.ui.activities.*
import com.example.shopperista.activities.ui.fragments.DashboardFragment
import com.example.shopperista.activities.ui.fragments.OrdersFragment
import com.example.shopperista.activities.ui.fragments.ProductsFragment
import com.example.shopperista.activities.ui.fragments.SoldProductsFragment
import com.example.shopperista.models.*
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.ImageFileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID() : String{

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = " "
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun registerUser(activity : RegisterActivity, userInfo : User){

        mFireStore.collection(Constants.USERS)
            .document(userInfo.id!!)
            .set(userInfo,SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while registering the user")
            }

    }

    fun getUserDetails(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(Constants.SHOP_PREFERENCES, Context.MODE_PRIVATE)

                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME,"${user.firstName} ${user.lastName}")
                editor.apply()

                when(activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }

                    is SettingsActivity ->{
                        activity.userDetailsSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }

    }

    fun updateUserProfileData(activity:Activity, userHashMap : HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{
                when(activity) {
                    is UserProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while updating the user")
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri : Uri?,imageType : String){

        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + ImageFileExtension(activity,imageFileUri)
        )

        sRef.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URl",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image Uri", uri.toString())
                        when(activity){
                            is UserProfileActivity ->{
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity ->{
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener{exception ->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName,exception.message,exception)
            }

    }

    fun uploadImageToCloudStorageUpdated(activity: Activity, imageFileUri : Uri?,imageType : String){

        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + ImageFileExtension(activity,imageFileUri)
        )

        sRef.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URl",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image Uri", uri.toString())
                        when(activity){
                            is AddProductActivity ->{
                                activity.imageUploadSuccessUpdated(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener{exception ->
                when(activity){
                    is AddProductActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName,exception.message,exception)
            }

    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo : Product){

        mFireStore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while uploading the product details")
            }
    }

    fun updateProduct(activity: AddProductActivity,productHashmap:HashMap<String,Any>){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productHashmap[Constants.PRODUCT_ID].toString())
            .update(productHashmap)
            .addOnSuccessListener {
                activity.productUpdated()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while updating the user")
            }
    }

    fun getProductsList(fragment: Fragment){
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Product list",document.documents.toString())

                val productList : ArrayList<Product> = ArrayList()

                for(i in document.documents){
                    val product = i.toObject(Product::class.java)!!

                    val productIdHashMap = HashMap<String,Any>()
                    productIdHashMap[Constants.PRODUCT_ID]=i.id
                    mFireStore.collection(Constants.PRODUCTS)
                        .document(i.id)
                        .update(productIdHashMap)

                    productList.add(product)
                }

                when(fragment){
                    is ProductsFragment -> {
                        fragment.successProductsListFromFirestore(productList)
                    }
                }
            }
    }

    fun deleteProduct(fragment: ProductsFragment,productID : String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccessMethod()
            }
            .addOnFailureListener{ e->
                fragment.hideProgressDialog()
                Log.e(fragment.requireActivity().javaClass.simpleName,"Error while deleting the product",e)
            }
    }

    fun getDashboardItems(fragment : DashboardFragment, itemType : String){

        when(itemType){
            Constants.ALL_ITEMS -> {
                mFireStore.collection(Constants.PRODUCTS)
                    .get()
                    .addOnSuccessListener { document ->
                        val productList : ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)!!

                            val productIdHashMap = HashMap<String,Any>()
                            productIdHashMap[Constants.PRODUCT_ID]=i.id
                            mFireStore.collection(Constants.PRODUCTS)
                                .document(i.id)
                                .update(productIdHashMap)

                            productList.add(product)
                        }

                        fragment.successDashboardItemsList(productList,itemType)
                    }
                    .addOnFailureListener{
                        fragment.hideProgressDialog()
                        Log.e(fragment.javaClass.simpleName,"error while getting dashboard items")
                    }
            }

            else -> {
                mFireStore.collection(Constants.PRODUCTS)
                    .whereEqualTo(Constants.CATEGORY,itemType)
                    .get()
                    .addOnSuccessListener { document ->
                        val productList : ArrayList<Product> = ArrayList()

                        for(i in document.documents){
                            val product = i.toObject(Product::class.java)!!

                            val productIdHashMap = HashMap<String,Any>()
                            productIdHashMap[Constants.PRODUCT_ID]=i.id
                            mFireStore.collection(Constants.PRODUCTS)
                                .document(i.id)
                                .update(productIdHashMap)

                            productList.add(product)
                        }

                        fragment.successDashboardItemsList(productList,itemType)
                    }
                    .addOnFailureListener{
                        fragment.hideProgressDialog()
                        Log.e(fragment.javaClass.simpleName,"error while getting dashboard items")
                    }

            }
        }
    }

    fun getProductDetails(activity: ProductDetailsActivity , productID: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productID)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.toString())
                val product= document.toObject(Product::class.java)!!
                activity.productDetailsSuccess(product)
            }
            .addOnFailureListener{ e->
                Log.e(activity.javaClass.simpleName,"Error while getting product details",e)
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart : CartItem){
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener{e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while creating document for cart item",e)
            }

    }

    fun checkIfItemExistsInCart(activity: ProductDetailsActivity,productID: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID,productID)
            .get()
            .addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                if(document.documents.size>0){
                    activity.productExistsInCart()
                }
                else{
                    activity.productNotInCart()
                }
            }
            .addOnFailureListener{ e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while checking existing cart item",e)
            }
    }

    fun getCartList(activity: Activity){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val list: ArrayList<CartItem> = ArrayList()

                for(i in document.documents){

                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.cart_id=i.id

                    val cartItemIdHashmap = HashMap<String,Any>()
                    cartItemIdHashmap[Constants.CART_ID]=i.id
                    mFireStore.collection(Constants.CART_ITEMS)
                        .document(i.id)
                        .update(cartItemIdHashmap)

                    list.add(cartItem)
                }
                when(activity){
                    is CartListActivity -> {
                        activity.successCartItemsList(list)
                    }
                    is CheckoutActivity ->{
                        activity.successCartItemsList(list)
                    }
                }
            }
            .addOnFailureListener{ e->
                when(activity){
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while getting cart list items",e)
            }
    }

    fun removeItemFromCart(context : Context , cart_id : String){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName,"Error while removing item from cart",e)
            }
    }

    fun updateMyCart(context : Context, cart_id : String, itemHashMap : HashMap<String, Any>){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity ->{
                        context.itemUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener{ e ->
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName,"Error while updating cart item",e)

            }
    }

    fun addAddress( activity: AddEditAddressActivity , addressInfo : Address) {
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener{ e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }
    }

    fun getAddressesList(activity: AddressListActivity){
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val addressList : ArrayList<Address> = ArrayList()
                for(i in document.documents){
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    val addressHashMap = HashMap<String,Any>()
                    addressHashMap[Constants.ADDRESS_ID]=i.id
                    mFireStore.collection(Constants.ADDRESSES)
                        .document(i.id)
                        .update(addressHashMap)

                    addressList.add(address)

                }
                activity.successAddressListFromFireStore(addressList)
            }
            .addOnFailureListener{e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId : String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener{ e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while updating the address",e)
            }
    }

    fun deleteAddress(activity : AddressListActivity, addressId : String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteSuccess()
            }
            .addOnFailureListener{ e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while deleting the address.",e)
            }
    }

    fun placeOrder(activity: CheckoutActivity, order: Order){
        mFireStore.collection(Constants.ORDER)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error uploading the order",e)
            }
    }

    fun getMyOrdersList(fragment : OrdersFragment){
        mFireStore.collection(Constants.ORDER)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                val list : ArrayList<Order> = ArrayList()

                for(i in document.documents){
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id
                    val orderHashMap = HashMap<String,Any>()
                    orderHashMap[Constants.ADDRESS_ID]=i.id
                    mFireStore.collection(Constants.ORDER)
                        .document(i.id)
                        .update(orderHashMap)

                    list.add(orderItem)
                }

                fragment.populateOrderListInUI(list)
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while updating details",e)
            }

    }

    fun updateAllDetails(activity : CheckoutActivity, cartList : ArrayList<CartItem>, order: Order){

        val writeBatch = mFireStore.batch()

        for(cartItem in cartList){

            val soldProduct = SoldProduct(
                cartItem.product_owner_id!!,
                cartItem.title!!,
                cartItem.price!!,
                cartItem.cart_quantity!!,
                cartItem.image!!,
                order.order_title!!,
                order.date!!,
                order.sub_total_amount!!,
                order.shipping_charge!!,
                order.total_amount!!,
                order.address!!
            )

            val documentReference = mFireStore.collection(Constants.SOLD_PRODUCT)
                .document()
            writeBatch.set(documentReference,soldProduct)
        }

        for (cart in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.STOCK_QUANTITY] =
                (cart.stock_quantity!!.toInt() - cart.cart_quantity!!.toInt()).toString()

            val documentReference = mFireStore.collection(Constants.PRODUCTS)
                .document(cart.product_id!!)

            writeBatch.update(documentReference, productHashMap)
        }

        for(cartItem in cartList){
            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.cart_id!!)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.addDetailsUpdatedSuccessfully()
        }.addOnFailureListener { e->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName,"Error while updating details",e)
        }
    }

    fun getSoldProductsList(fragment : SoldProductsFragment){
        mFireStore.collection(Constants.SOLD_PRODUCT)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<SoldProduct> = ArrayList()

                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldProduct::class.java)!!

                    val soldProductIdHashMap = HashMap<String,Any>()
                    soldProductIdHashMap[Constants.ID] = i.id
                    mFireStore.collection(Constants.SOLD_PRODUCT)
                        .document(i.id)
                        .update(soldProductIdHashMap)

                    list.add(soldProduct)
                }

                fragment.successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while updating details",e)
            }
    }
}