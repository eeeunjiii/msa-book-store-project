package com.example.presentation;

import com.example.constant.Payment;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @ModelAttribute("payments")
    public Payment[] payments() {
        return Payment.values();
    }

    /** Item에서 바로 주문하기로 주문한 경우 **/
    @PostMapping("/items/order")
    public ResponseEntity<ApiResponse<CompleteOrderResponse>> createOrderFromItem(
            @RequestHeader("X-User-Email") String email,
            @RequestBody OrderInfoRequest orderInfoRequest,
            @RequestParam("quantity") int quantity,
            @RequestParam("itemId") Long itemId) {
        CompleteOrderResponse response=orderService.createOrder(email, itemId, quantity, orderInfoRequest);

        log.info("User Email: {} | ", email);

        return ResponseEntity.ok(ApiResponse.success(response, "도서 바로 주문하기 완료"));
    }

    /** 카트에서 주문하는 경우 **/
    @GetMapping("/items/cart/order")
    public ResponseEntity<ApiResponse<Integer>> orderFormFromCart(HttpSession session) { // 아직 OrderItem으로 저장하기 전 -> 미리 주문 내역 출력
        List<CreateOrderItemRequest> orderItemRequests=(List<CreateOrderItemRequest>) session.getAttribute("orderItems");

        int totalPrice=orderService.getTotalPrice(orderItemRequests);

        return ResponseEntity.ok(ApiResponse.success(totalPrice));
    }

    @PostMapping("/items/cart/order")
    public ResponseEntity<ApiResponse<CompleteOrderResponse>> createOrderFromCart( // 실제 주문이 완료되는 곳
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody OrderInfoRequest orderInfoRequest,
            HttpServletRequest request) {
        HttpSession session=request.getSession();
        List<CreateOrderItemRequest> orderItemRequests=
                (List<CreateOrderItemRequest>) session.getAttribute("orderItems");

        CompleteOrderResponse response=orderService.createOrder(email, orderItemRequests, orderInfoRequest);

        return ResponseEntity.ok(ApiResponse.success(response, "장바구니 주문하기 완료"));
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse<List<CompleteOrderResponse>>> orderListForm(
            @AuthenticationPrincipal(expression = "username") String email) {
        List<CompleteOrderResponse> responses=orderService.findOrderListByUser(email);

        return ResponseEntity.ok(ApiResponse.success(responses, "주문 목록 조회 완료"));
    }

    @GetMapping("/{userId}/order/{orderId}")
    public ResponseEntity<ApiResponse<List<CompleteOrderItemResponse>>> orderDetailsForm(@PathVariable("orderId") Long orderId) {
        List<CompleteOrderItemResponse> responses=orderItemService.findOrderItemList(orderId);

        return ResponseEntity.ok(ApiResponse.success(responses, "상세 주문 내역 조회 완료"));
    }

    @PostMapping("/items/add/order")
    public ResponseEntity<ApiResponse<String>> addToOrder(HttpServletRequest request,
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
        return ResponseEntity.ok(ApiResponse.success("선택 도서 주문 추가 완료"));
    }

    @PostMapping("/items/delete/order")
    public ResponseEntity<ApiResponse<String>> deleteFromOrder(HttpServletRequest request,
                                                  @RequestBody OrderItemRequest orderItemRequest) {
        HttpSession session=request.getSession();
        List<OrderItemRequest> orderItemDtoList=(List<OrderItemRequest>) session.getAttribute("orderItems");

        if(orderItemDtoList!=null) {
            orderItemDtoList.removeIf(orderItem -> orderItem.getItemId().equals(orderItemRequest.getItemId()));
            session.setAttribute("orderItems", orderItemDtoList);
        }

        return ResponseEntity.ok(ApiResponse.success("선택 도서 주문 제거 완료"));
    }
}
