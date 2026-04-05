package com.example.presentation;

import com.example.domain.Item;
import com.example.application.ItemService;
import com.example.domain.User;
import com.example.domain.Cart;
import com.example.domain.CartItem;
import com.example.request.CartRequest;
import com.example.security.PrincipalDetails;
import com.example.application.CartItemService;
import com.example.application.CartService;
import com.example.application.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ItemService itemService;
    private final CartItemService cartItemService;

    @GetMapping("/{userId}")
    public String cartForm(@PathVariable("userId") Long userId,
                           Model model) {
        Cart cart=cartService.findCartByUserId(userId);
        List<CartItem> cartItems = cartItemService.findCartItemsByCart(cart.getId());

        int totalPrice=0;
        for(CartItem cartItem:cartItems) {
            Item item=itemService.findById(cartItem.getItemId());
            totalPrice+=cartItem.getQuantity()*item.getPrice();
        }

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartItems);

        return "user/cartForm";
    }

    @PostMapping("/items/{itemId}/add")
    public ResponseEntity<String> addItemToCartForm(@PathVariable("itemId") Long itemId,
                                                    @AuthenticationPrincipal PrincipalDetails principal,
                                                    @RequestBody CartRequest cartRequest,
                                                    RedirectAttributes redirectAttributes) {
        User user=userService.findUserByEmail(principal.getUsername());
        Item item=itemService.findById(itemId);
        int quantity= cartRequest.getQuantity();

        cartService.addItemToCart(user.getId(), item, quantity);

        redirectAttributes.addAttribute("itemId", itemId);

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    @PostMapping("/items/{itemId}/delete")
    public ResponseEntity<String> deleteItemFromCart(@PathVariable("itemId") Long iemId,
                                                     @AuthenticationPrincipal PrincipalDetails principal) {
        User user = userService.findUserByEmail(principal.getUsername());
        Long cartId = cartService.findCartByUserId(user.getId()).getId();
        Item item = itemService.findById(iemId);

        CartItem removeCartItem = cartItemService.findByCartIdAndItemId(cartId, item.getId());
        cartItemService.delete(removeCartItem);

        return ResponseEntity.ok("Success in removing item from cart");
    }
}
