/*
 *  Copyright (c) 2017 Kamesh Sampath<kamesh.sampath@hotmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.workspace7.moviestore.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.workspace7.moviestore.data.Movie;
import org.workspace7.moviestore.data.MovieCart;
import org.workspace7.moviestore.data.MovieCartItem;
import org.workspace7.moviestore.utils.MovieDBHelper;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author kameshs
 */
@Controller
@Slf4j
@Data
public class ShoppingCartController {
    
    final String hostName = System.getenv().getOrDefault("HOSTNAME", "unknown");

    public static final String SESSION_ATTR_MOVIE_CART = "MOVIE_CART";
    @Autowired
    MovieDBHelper movieDBHelper;

    /**
     * @param movieId
     * @param qty
     * @param session
     * @return
     */
    @GetMapping("/cart/add")
    public @ResponseBody
    String addItemToCart(@RequestParam("movieId") String movieId, @RequestParam("quantity") int qty,
                         HttpSession session) {

        MovieCart movieCart;

        if (session.getAttribute(SESSION_ATTR_MOVIE_CART) == null) {
            log.info("No Cart Exists for the session, creating one");
            movieCart = new MovieCart();
            movieCart.setOrderId(UUID.randomUUID().toString());
        } else {
            log.info("Cart Exists for the session, will be updated");
            movieCart = (MovieCart) session.getAttribute(SESSION_ATTR_MOVIE_CART);
        }

        log.info("Adding/Updating {} with Quantity {} to cart ", movieId, qty);

        Map<String, Integer> movieItems = movieCart.getMovieItems();

        if (movieItems.containsKey(movieId)) {
            movieItems.replace(movieId, qty);
        } else {
            movieItems.put(movieId, qty);
        }

        log.info("Movie Cart:{}", movieCart);

        //update the session back
        session.setAttribute(SESSION_ATTR_MOVIE_CART, movieCart);

        return String.valueOf(movieCart.getMovieItems().size());
    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/cart/show")
    public String showCart(Map<String, Object> model, HttpSession session) {

        MovieCart movieCart = (MovieCart) session.getAttribute(SESSION_ATTR_MOVIE_CART);

        log.info("Showing Cart {}", movieCart);

        model.put("movieCart", movieCart);

        Map<String, Integer> movieItems = movieCart.getMovieItems();
        List<MovieCartItem> cartMovies = movieCart.getMovieItems().keySet().stream()
            .map(movieId -> {
                Movie movie = movieDBHelper.query(movieId);
                int quantity = movieItems.get(movieId);
                double total = quantity * movie.getPrice();
                log.info("Movie:{} total for {} items is {}", movie, quantity, total);
                return MovieCartItem.builder()
                    .movie(movie)
                    .quantity(quantity)
                    .total(total)
                    .build();
            })
            .collect(Collectors.toList());
        model.put("hostName", hostName);    
        model.put("cartItems", cartMovies);
        model.put("cartCount", cartMovies.size());
        return "cart";
    }

    /**
     *
     */
    @PostMapping("/cart/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public void checkout(HttpSession session) {
        MovieCart movieCart = (MovieCart) session.getAttribute(SESSION_ATTR_MOVIE_CART);
        log.info("Your request {} will be processed, thank your for shopping", movieCart);
    }
}
