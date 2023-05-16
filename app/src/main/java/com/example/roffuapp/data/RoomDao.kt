package com.example.roffuapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.roffuapp.models.Advertisement
import com.example.roffuapp.models.BookmarkItem
import com.example.roffuapp.models.BookmarkItemWithProduct
import com.example.roffuapp.models.CartItem
import com.example.roffuapp.models.CartItemWithProduct
import com.example.roffuapp.models.LocalManufacturer
import com.example.roffuapp.models.Location
import com.example.roffuapp.models.Manufacturer
import com.example.roffuapp.models.Order
import com.example.roffuapp.models.OrderDetails
import com.example.roffuapp.models.OrderItem
import com.example.roffuapp.models.OrderPayment
import com.example.roffuapp.models.PaymentProvider
import com.example.roffuapp.models.Product
import com.example.roffuapp.models.ProductColor
import com.example.roffuapp.models.ProductDetails
import com.example.roffuapp.models.ProductSize
import com.example.roffuapp.models.Review
import com.example.roffuapp.models.User
import com.example.roffuapp.models.UserPaymentProvider
import com.example.roffuapp.models.UserPaymentProviderDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    /** Manufacturer operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManufacturer(manufacturer: Manufacturer)

    @Transaction
    @Query("SELECT * FROM manufacturer")
    suspend fun getManufacturersWithProducts(): List<LocalManufacturer>

    /** Products operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherProductCopy(productColor: ProductColor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSize(productSize: ProductSize)

    /** Advertisements operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdvertisement(advertisement: Advertisement)

    @Query("SELECT * FROM advertisement")
    suspend fun getAdvertisements(): List<Advertisement>

    /** All Cart operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Transaction
    @Query("SELECT * from cart")
    fun getCartItems(): Flow<MutableList<CartItemWithProduct>>

    @Query("SELECT productId FROM cart")
    fun getCartProductsIds(): Flow<List<Int>>

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Query("UPDATE cart SET quantity = :quantity WHERE productId = :productId ")
    suspend fun updateCartItemQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart WHERE productId = :productId ")
    suspend fun deleteCartItem(productId: Int)

    @Query("Delete FROM cart")
    suspend fun clearCart()

    /** Payment providers operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePaymentProvider(paymentProvider: PaymentProvider)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPaymentProvider(userPaymentProvider: UserPaymentProvider)

    @Transaction
    @Query("SELECT * FROM userPaymentProviders")
    suspend fun getUserPaymentProviders(): List<UserPaymentProviderDetails>

    /** Order operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderPayment(payment: OrderPayment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Transaction
    @Query("SELECT * FROM orders")
    suspend fun getLocalOrders(): List<OrderDetails>

    /** Bookmarks operations */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookmarkItem(bookmarkItem: BookmarkItem)

    @Transaction
    @Query("SELECT * from bookmarks")
    fun getBookmarkItems(): Flow<MutableList<BookmarkItemWithProduct>>

    @Query("SELECT productId FROM bookmarks")
    fun getBookmarkProductsIds(): Flow<List<Int>>

    @Query("DELETE FROM bookmarks WHERE productId = :productId ")
    suspend fun deleteBookmarkItem(productId: Int)

    @Query("Delete FROM bookmarks")
    suspend fun clearBookmark()

    /** Product reviews */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<Review>)

    /** User operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user WHERE userId = :userId LIMIT 1")
    suspend fun getLoggedUser(userId: Int): User?

    @Transaction
    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductDetails(productId: Int): ProductDetails?

    /** Locations operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: Location)

    @Query("SELECT * FROM location")
    suspend fun getUserLocations(): List<Location>

    @Query("UPDATE orders SET isDelivered = :delivered")
    suspend fun updateOrdersAsDelivered(delivered: Boolean = true)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun fakeSignIn(email: String, password: String): User?
}