package com.example.roffuapp.repositories

import com.example.roffuapp.data.RoomDao
import com.example.roffuapp.models.BookmarkItem
import com.example.roffuapp.models.CartItem
import com.example.roffuapp.models.Order
import com.example.roffuapp.models.OrderDetails
import com.example.roffuapp.models.OrderItem
import com.example.roffuapp.models.OrderPayment
import com.example.roffuapp.models.Product
import com.example.roffuapp.sealed.DataResponse
import com.example.roffuapp.sealed.Error
import com.example.roffuapp.utils.UserPref
import com.example.roffuapp.utils.getFormattedDate
import com.example.roffuapp.utils.getStructuredProducts
import java.util.*
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val dao: RoomDao,
) {

    suspend fun updateCartState(productId: Int, alreadyOnCart: Boolean) {
        /** Handle the local storing process */
        handleLocalCart(productId = productId, alreadyOnCart = alreadyOnCart)
        /** Handle the remote process */
    }

    private suspend fun handleLocalCart(productId: Int, alreadyOnCart: Boolean) {
        if (alreadyOnCart) {
            /** Already on local , delete it */
            dao.deleteCartItem(productId = productId)
        } else {
            /** not on local , add it */
            val cartItem = CartItem(
                productId = productId,
                quantity = 1,
            )
            addToLocalCart(cartItem = cartItem)
        }
    }

    private suspend fun addToLocalCart(cartItem: CartItem) {
        /** Add it to cart items */
        dao.insertCartItem(cartItem = cartItem)
    }

    suspend fun updateCartItemQuantity(id: Int, quantity: Int) {
        /** Update local cart item quantity */
        dao.updateCartItemQuantity(productId = id, quantity = quantity)
    }

    suspend fun saveOrders(
        items: List<CartItem>,
        providerId: String?,
        total: Double,
        deliveryAddressId: Int?,
        onFinished: () -> Unit,
    ) {
        UserPref.user.value?.let {
            val order = Order(
                orderId = Date().getFormattedDate("yyyyMMddHHmmSS"),
                userId = it.userId,
                total = total,
                locationId = deliveryAddressId,
            )
            val orderPayment = OrderPayment(
                orderId = order.orderId,
                providerId = providerId,
            )

            /** Fake the success of delivering the previous order */
            dao.updateOrdersAsDelivered()

            dao.insertOrder(order = order)
            dao.insertOrderPayment(payment = orderPayment)
            dao.insertOrderItems(
                items = items.map { cartItem ->
                    OrderItem(
                        orderId = order.orderId,
                        quantity = cartItem.quantity,
                        productId = cartItem.productId,
                        userId = it.userId,
                    )
                },
            )
            /** Then clear our cart */
            dao.clearCart()
            onFinished()
        }
    }

    fun getCartProductsIdsFlow() = dao.getCartProductsIds()

    fun getLocalCart() = dao.getCartItems()

    suspend fun clearCart() {
        dao.clearCart()
    }

    fun getBookmarksProductsIdsFlow() = dao.getBookmarkProductsIds()

    suspend fun updateBookmarkState(productId: Int, alreadyOnBookmark: Boolean) {
        /** Handle the local storing process */
        handleLocalBookmark(productId = productId, alreadyOnBookmark = alreadyOnBookmark)
        /** Handle the remote process */
    }

    private suspend fun handleLocalBookmark(productId: Int, alreadyOnBookmark: Boolean) {
        if (alreadyOnBookmark) {
            /** Already on local , delete it */
            dao.deleteBookmarkItem(productId = productId)
        } else {
            /** Add it to bookmark items */
            dao.insertBookmarkItem(bookmarkItem = BookmarkItem(productId = productId))
        }
    }

    fun getLocalBookmarks() = dao.getBookmarkItems()

    suspend fun getProductDetails(productId: Int): DataResponse<Product> {
        /** Check the local storage */
        dao.getProductDetails(productId = productId)?.let {
            return DataResponse.Success(data = it.getStructuredProducts())
        }
        /** Doesn't exist on the local, check remote */
        return DataResponse.Error(error = Error.Network)
    }

    suspend fun getOrdersHistory(): DataResponse<List<OrderDetails>> {
        /** Check the local storage */
        dao.getLocalOrders().let {
            if (it.isNotEmpty())
                return DataResponse.Success(data = it)
        }
        /** Doesn't exist on the local, check remote */
        return DataResponse.Error(error = Error.Empty)
    }
}