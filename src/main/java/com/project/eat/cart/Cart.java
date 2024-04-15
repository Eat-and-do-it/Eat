package com.project.eat.cart;

import com.project.eat.domain.member.Member;
import com.project.eat.domain.order.OrderType;
import com.project.eat.domain.shop.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    private int totalPrice;

    @OneToMany(mappedBy = "cart" , cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void totalPrice() {
        int price = 0;
        for (CartItem cartItem : cartItems) {
            price  += cartItem.getPrice();
        }
        this.setTotalPrice(price);
    }

}
