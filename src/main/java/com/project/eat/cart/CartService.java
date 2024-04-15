package com.project.eat.cart;

import com.project.eat.domain.member.Member;
import com.project.eat.domain.shop.Shop;
import com.project.eat.member.MemberRepository;
import com.project.eat.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;

    public Cart findCart(Long cartId) {
        return cartRepository.findCart(cartId);
    }


    @Transactional
    public void createCart(String memberId) {

        Cart cart = new Cart();
        Member member = memberRepository.findOne(memberId);


        cart.setMember(member);


        cartRepository.save(cart);
        member.addCart(cart);

    }

    @Transactional
    public int getTotalPrice(String memberId) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.getCart().totalPrice();
        return findMember.getCart().getTotalPrice();
    }

    @Transactional
    public void deleteAndCreateCart(String memberId, Long shopId) {
        Member findMember = memberRepository.findOne(memberId);
        Cart findCart = findMember.getCart();
        cartRepository.delete(findCart);

        cartRepository.flush();
        log.info("context flush");
        Cart cart = new Cart();
        Member member = memberRepository.findOne(memberId);
        Shop shop = shopRepository.findShop(shopId);

        cart.setMember(member);
        cart.setShop(shop);

        cartRepository.save(cart);
        member.addCart(cart);

    }

    @Transactional
    public void deleteCart(String memberId) {
        Member findMember = memberRepository.findOne(memberId);
        Cart cart = findMember.getCart();
        cartRepository.delete(cart);

    }

    public int countCartItems(String memberId) {
        Member findMember = memberRepository.findOne(memberId);
        return findMember.getCart().getCartItems().size();
    }

    public Long findShopId(String memberId) {
        Member findMember = memberRepository.findOne(memberId);
        if(findMember.getCart().getShop() !=null){
            return findMember.getCart().getShop().getId();
        }
        return  null;
    }

    public void remove(String memberId) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.getCart();
    }

    @Transactional
    public void setShopCart(String memberId, Long shopId) {
        Member findMember = memberRepository.findOne(memberId);


        Shop shop = shopRepository.findShop(shopId);

        findMember.getCart().setShop(shop);
    }

    @Transactional
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}
