# Game21-A-Network-Multiplayer-Game-In-MVC

This is a multiplayer network game in MVC.
Game is very similar to a BlackJack cards game.
Server is a Dealer.
Client is a player in the game.

Rules of Game:
1. N number of players can play one round of game at a time.
2. Dealer serves you 2 cards for the first time to each player.
3. Any point of the game if the total numeric value of cards in your hand goes over 21, you are busted and hence you loss.
4. Numberic value of cards is calculated as 2-10 for face of card 2-10 and J,Q,K are evaluated as 10,11,12 and
A can be evaluated either as 1 or 11 to your advatage.
5.each player is asked for a move each time, he can choose HIT, DOUBLE, HOLD.
HIT: Dealer serves you one more card
DOUBLE: Dealear serves you one card and this is the last card you get and you are on hold after this
HOLD: You dont want any more cards and you HOLD till all bust or hold
6. Winner of the game is the one who's card on hand has highest value without busting(sum less than 22).
7. if two or more player has highest sum then it is a Draw

How to play game.
1.On one Device Run ServerApp.
2.On other Devices Run ClientApp
3.you need to enter how many players on table on Server Side.
4.On Client Side you need to feed your name and Money you have and then server Ip.
5.As prompted on screen enter your moves
