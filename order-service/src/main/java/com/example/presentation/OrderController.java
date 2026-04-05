package com.example.presentation;

import com.example.adapter.UserClient;
import com.example.constant.Payment;
import com.example.domain.OrderItem;
import com.example.request.CreateOrderItemRequest;
import com.example.request.OrderInfoRequest;
import com.example.response.*;
import com.example.request.OrderItemRequest;
import com.example.application.OrderItemService;
import com.example.application.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserClient userClient;

    @ModelAttribute("payments")
    public Payment[] payments() {
        return Payment.values();
    }

    /** Item에서 바로 주문하기로 주문한 경우 **/
    @PostMapping("/items/order")
    public ResponseEntity<CompleteOrderResponse> createOrderFromItem(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody OrderInfoRequest orderInfoRequest,
            @RequestParam("quantity") int quantity,
            @RequestParam("itemId") Long itemId) {
        log.info("OrderController | email: {}", email);
        UserResponse userResponse=userClient.sendUserResponse(email);

        log.info("email: {} | phoneNumber: {}", userResponse.getEmail(), userResponse.getPhoneNum());

        CompleteOrderResponse completeOrderResponse=
                orderService.createOrder(userResponse.getId(), itemId, quantity, orderInfoRequest);

        return ResponseEntity.ok(completeOrderResponse);
    }

    /** 카트에서 주문하는 경우 **/
    @GetMapping("/items/cart/order")
    public ResponseEntity<Integer> orderFormFromCart(HttpSession session,
                                                     @RequestBody OrderInfoRequest orderInfoRequest) { // 아직 OrderItem으로 저장하기 전 -> dto로 미리 주문 내역 출력
        List<CreateOrderItemRequest> orderItemDtoList=(List<CreateOrderItemRequest>) session.getAttribute("orderItems");

        int totalPrice=orderService.getTotalPrice(orderItemDtoList);

        return ResponseEntity.ok(totalPrice);
    }

    @PostMapping("/items/cart/order")
    public ResponseEntity<CompleteOrderResponse> createOrderFromCart( // 실제 주문이 완료되는 곳
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody OrderInfoRequest orderInfoRequest,
            HttpServletRequest request) {
        HttpSession session=request.getSession();
        List<CreateOrderItemRequest> orderItemRequests=
                (List<CreateOrderItemRequest>) session.getAttribute("orderItems");

        CompleteOrderResponse completeOrderResponse
                =orderService.createOrder(email, orderItemRequests, orderInfoRequest);

        return ResponseEntity.ok(completeOrderResponse);
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<List<CompleteOrderResponse>> orderListForm(
            @AuthenticationPrincipal(expression = "username") String email) {
        List<CompleteOrderResponse> completeOrderResponses=
                orderService.findOrderListByUser(email);

        return ResponseEntity.ok(completeOrderResponses);
    }

    @GetMapping("/{userId}/order/{orderId}")
    public String orderDetailsForm(Model model, @PathVariable("orderId") Long orderId) {
        List<OrderItem> orderItems=orderItemService.findListByOrderId(orderId);

        model.addAttribute("orderItems", orderItems);

        return "/user/orderItemForm";
    }

    @PostMapping("/items/add/order")
    public ResponseEntity<String> addToOrder(HttpServletRequest request,
                                             @RequestBody OrderItemRequest orderItemRequest) {
        HttpSession session=request.getSession();
        List<OrderItemRequest> orderItemRequestList=(List<OrderItemRequest>) session.getAttribute("orderItems");

        if(orderItemRequestList==null) {
            orderItemRequestList=new ArrayList<>();
        }

        if(!orderItemRequestList.contains(orderItemRequest)) {
            orderItemRequestList.add(orderItemRequest);
        }

        session.setAttribute("orderItems", orderItemRequestList);
        return ResponseEntity.ok("Success add to order");
    }

    @PostMapping("/items/delete/order")
    public ResponseEntity<String> deleteFromOrder(HttpServletRequest request,
                                                  @RequestBody OrderItemRequest orderItemRequest) {
        HttpSession session=request.getSession();
        List<OrderItemRequest> orderItemDtoList=(List<OrderItemRequest>) session.getAttribute("orderItems");

        if(orderItemDtoList!=null) {
            orderItemDtoList.removeIf(orderItem -> orderItem.getItemId().equals(orderItemRequest.getItemId()));
            session.setAttribute("orderItems", orderItemDtoList);
        }

        return ResponseEntity.ok("Success remove from order");
    }
}
