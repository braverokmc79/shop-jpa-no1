package com.shop.controller;

import com.shop.config.auth.PrincipalDetails;
import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;

    @GetMapping(value = "/cart")
    public String orderHist(@AuthenticationPrincipal PrincipalDetails principal, Model model){

        log.info("****장바구니 PrincipalDetails  {}", principal.getMember());

        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getEmail());
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    }


    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity<?> order(@RequestBody @Valid CartItemDto cartItemDto
            , BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal) {

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getEmail();
        Long cartItemid;

        try {
            cartItemid = cartService.addCart(cartItemDto, email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemid, HttpStatus.OK);
    }




}
