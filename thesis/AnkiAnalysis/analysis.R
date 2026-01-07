#install.packages("plyr")
#install.packages("dplyr")
#install.packages("ggplot2")
#install.packages("plotrix")
#install.packages("Rmisc")
library(Rmisc)
library(plyr)
library(dplyr)
library(ggplot2)
library(plotrix)
####################################################################
# Get and filter users
####################################################################
c_users_path <- "data/connection_users.csv"
i_users_path <- "data/independent_users.csv"
c_logs_path <- "data/connection_logs.csv"
i_logs_path <- "data/independent_logs.csv"

c_users = read.csv(c_users_path) %>%
  filter((date != '' | grepl('lena55', nickName)) &
           (!grepl('anse23', nickName) & !grepl('player730', nickName) & !grepl('player993', nickName))) %>%
  filter(logs > 5)

i_users = read.csv(i_users_path) %>%
  filter(date != '' & !grepl('Mike', nickName)) %>%
  filter(logs > 0)

####################################################################
# Get users from the general public
####################################################################
# c_users_path <- "data/public_connection_users.csv"
# i_users_path <- "data/public_independent_users.csv"
# c_logs_path <- "data/public_connection_logs.csv"
# i_logs_path <- "data/public_independent_logs.csv"
#
# c_users = read.csv(c_users_path)
# i_users = read.csv(i_users_path)

####################################################################
# Get and fitler the logs
####################################################################

get_logs <- function(path, users) {
  # Read and filter by valid users
  logs = read.csv(path) %>%
    filter(userId %in% users$id)

  # update date format
  logs$date <- as.Date(logs$date, "%Y/%m/%d")

  # Filter by period of study
  logs <- logs %>%
    filter(date >= "2018/06/18" & date <= "2018/07/15")

  return (logs)
}

c_logs = get_logs(c_logs_path, c_users)
i_logs = get_logs(i_logs_path, i_users)

####################################################################
# Compare the number of earned coins in both groups
####################################################################
# Filter the logs
c_logs_assess <- c_logs %>%
  filter(logType == 'assessCard')
i_logs_assess <- i_logs %>%
  filter(logType == 'assessCard')

# Plot the comparison in an histogram
c_earned_coins_hist <- hist(c_logs_assess$coinsInCard, plot=FALSE)
i_earned_coins_hist <- hist(i_logs_assess$coinsInCard, plot=FALSE)
plot(0,0,type="n",xlim=c(0,6),ylim=c(0,1000),
     xlab="Earned coins in cards",ylab="",main="Frecuency of earned coins")
plot(c_earned_coins_hist,col="green",density=20,angle=135,add=TRUE)
plot(i_earned_coins_hist,col="blue",density=10,angle=45,add=TRUE)

####################################################################
# Compare the number of earned points in both groups
####################################################################
# Plot the comparison in an histogram
points_bin <- 30
c_earned_points_hist <- hist(c_logs_assess$pointsInCard, plot=FALSE, breaks=points_bin)
i_earned_points_hist <- hist(i_logs_assess$pointsInCard, plot=FALSE, breaks=points_bin)
plot(0,0,type="n",xlim=c(0,points_bin),ylim=c(0,1000),
     xlab="Earned points in cards",ylab="",main="Frecuency of earned points")
plot(c_earned_points_hist,col="green",density=20,angle=135,add=TRUE)
plot(i_earned_points_hist,col="blue",density=10,angle=45,add=TRUE)

####################################################################
# Compare the ease of cards in both groups
####################################################################
c_ease_hist <- hist(c_logs_assess$cardEase, plot=FALSE)
i_ease_hist <- hist(i_logs_assess$cardEase, plot=FALSE)
plot(0,0,type="n",xlim=c(0,4),ylim=c(0,2000),
     xlab="Ease of cards",ylab="",main="Ease of cards")
plot(c_ease_hist,col="green",density=20,angle=135,add=TRUE)
plot(i_ease_hist,col="blue",density=10,angle=45,add=TRUE)

####################################################################
# Compare the variation of reviewed cards over the period study
# in both groups
####################################################################
all_logs_in_period <- function(logs, title) {
  # Group the logs by user, type and date
  logs_by_user <- group_by(logs, userId, date)
  dplyr::summarize(logs_by_user, count=n()) %>%
    ggplot(aes(x=date, y=count)) +
    geom_point() +
    ggtitle(title) +
    labs(x = "Period of study", y = "Number of interactions") +
    facet_wrap(~userId) +
    xlim(as.Date(c('2018/06/18', '2018/07/16'), format="%Y/%m/%d") ) +
    theme(
      strip.background = element_blank(),
      strip.text.x = element_blank()
    )
}
logs_in_period <- function(logs, log_types, log_aliases, title) {
  # Group the logs by user, type and date
  logs_by_user <- group_by(logs, userId, logType, date)
  dplyr::summarize(logs_by_user, count=n()) %>%
    filter(logType %in% log_types) %>%
    ggplot(aes(x=date, y=count, col=logType)) +
    geom_point() +
    scale_colour_discrete(name = "Interactions", labels = log_aliases) +
    labs(x = "Period of study", y = "Number of flashcards") +
    facet_wrap(~userId) +
    xlim(as.Date(c('2018/06/18', '2018/07/16'), format="%Y/%m/%d") ) +
    theme_linedraw() +
    theme_light() +
    theme(
      axis.text.x = element_text(angle = 90, hjust = 1, size=16),
      axis.text.y = element_text(size=16),
      title = element_text(size=16),
      legend.position = "none",
      strip.background = element_blank(),
      strip.text.x = element_blank()
    )
}
logs_in_period(c_logs, c('assessCard'), c('Reviewed flashcards'), 'Experimental group')
logs_in_period(i_logs, c('assessCard'), c('Reviewed flashcards'), 'Control group')

####################################################################
# Compare the variation of checking leaderboard over the period study
# in both groups
####################################################################
logs_in_period(c_logs, c('checkLeaderboard'), c('Check leaderboard'), 'Experimental group')
logs_in_period(i_logs, c('checkLeaderboard'), c('Check leaderboard'), 'Control group')

####################################################################
# Compare the checking the leaderboard and reviewing cards
####################################################################
logs_in_period(c_logs, c('assessCard', 'checkLeaderboard'), c('Review card', 'Check leaderboard'), 'Experimental group')
logs_in_period(i_logs, c('assessCard', 'checkLeaderboard'), c('Review card', 'Check leaderboard'), 'Control group')

####################################################################
# Compare the playing the game and studying
####################################################################
logs_in_period(c_logs, c('goToGame', 'selectDeck'), c('Play', 'Review cards'), 'Experimental group')
logs_in_period(i_logs, c('selectDeck'), c('Review cards'), 'Control group')

####################################################################
# Compare all interactions in period
####################################################################
all_logs_in_period(c_logs, 'Experimental group')
all_logs_in_period(i_logs, 'Control group')

####################################################################
# Compare the average interactions
####################################################################
summ_logs_in_period <- function(logs) {
  ################################################################
  # Average number of interactions per user
  ################################################################
  logs_by_user <- group_by(logs, userId)
  summ <- dplyr::summarize(logs_by_user, count= n())
  r_ints <- as.integer(mean(summ$count))

  ################################################################
  # Average number of days per user
  ################################################################
  logs_by_user <- group_by(logs, userId, date)
  summ <- dplyr::summarize(logs_by_user, count= n())
  r_days <- as.integer(mean(count(summ)$n))

  ################################################################
  # Average interactions per day per user
  ################################################################
  logs_by_user <- group_by(logs, userId, date)
  summ <- dplyr::summarize(logs_by_user, count= n())
  days <- count(summ)
  interactions <- aggregate(summ$count, by=list(Category=summ$userId), FUN=sum)
  r_int_days <- as.integer(mean(interactions$x /days$n))
  return(c(r_ints, r_days, r_int_days))
}

summ_logs_in_period(i_logs)
summ_logs_in_period(c_logs)

summ_logs_in_period(i_logs %>% filter(logType=='assessCard'))
summ_logs_in_period(c_logs %>% filter(logType=='assessCard'))

summ_logs_in_period(i_logs %>% filter(logType=='selectDeck'))
summ_logs_in_period(c_logs %>% filter(logType=='selectDeck'))

####################################################################
# Compile data for users
####################################################################

per_user <- function(logs) {
  # interactions
  a <- group_by(logs, userId) %>%
    dplyr::summarize(interactions=n())

  # days using the app
  b <- group_by(logs, userId, date) %>%
    dplyr::summarize(days=n()) %>%
    count(userId) %>%
    summarize(days = n)

  # days between first and last use
  c <- group_by(logs, userId, date) %>%
    dplyr::summarize(count=n())
  d <- setNames(aggregate(c$date, by=list(c$userId), min), c("userId", "minDate"))
  e <- setNames(aggregate(c$date, by=list(c$userId), max), c("userId", "maxDate"))
  f <- merge(d, e) %>%
    group_by(userId) %>%
    summarize(period = as.numeric(maxDate - minDate + 1, units='days'))

  # interactions per day
  g <- merge(a,b) %>%
    group_by(userId) %>%
    summarize(interactions_day = round(interactions / days))

  # average days between sessions
  h <- merge(b, f) %>%
    group_by(userId) %>%
    summarize(days_between_sessions = period / days)

  # number of decks
  j <- group_by(logs, userId, deckName) %>%
    dplyr::summarize(decks=n()) %>%
    count(userId) %>%
    summarize(decks = n)

  # average elapsed time in card
  k <- group_by(logs, userId, logType, elapsedTime) %>%
    filter(logType=='assessCard')
  l <- setNames(aggregate(k$elapsedTime, by=list(k$userId), FUN=mean), c("userId", "meanElapsedTime"))

  # time in cards
  kk <- group_by(logs, userId, logType, elapsedTime) %>%
    filter(logType=='displayAnswerCard' || logType=='assessCard')
  ll <- setNames(aggregate(kk$elapsedTime, by=list(kk$userId), FUN=sum), c("userId", "timeInCards"))


  # earned points and coins
  m <- group_by(logs, userId)
  n <- aggregate(coinsInCard~userId, m, sum)
  o <- aggregate(pointsInCard~userId, m, sum)

  # remaining coins
  p <- group_by(logs, userId)
  q <- aggregate(totalCoins~userId, p, max)

  r <- merge(n, q) %>%
    group_by(userId) %>%
    summarize(spentCoins = coinsInCard - totalCoins + 10) # initial value

  # Total cards
  s <- group_by(logs, userId) %>%
    filter(logType == 'assessCard') %>%
    summarize(totalCards=n())

  # merge everything
  i <- merge(a, b) %>%
    merge(g) %>%
    merge(f) %>%
    merge(h) %>%
    merge(j) %>%
    merge(l,  all = TRUE) %>%
    merge(ll,  all = TRUE) %>%
    merge(n, all = TRUE) %>%
    merge(o, all = TRUE) %>%
    merge(q, all = TRUE) %>%
    merge(r, all = TRUE) %>%
    merge(s, all = TRUE)

  i[is.na(i)] <- 0

  return(i[order(i$interactions, decreasing=TRUE),])
}

c_users_logs <- per_user(c_logs)
i_users_logs <- per_user(i_logs)

i_users_logs["group"] <- "control"
i_users_logs

c_users_logs["group"] <-"experimental"
c_users_logs


c_users_logs  %>%
  summarise_if(is.numeric, funs(median, mean))

i_users_logs  %>%
  summarise_if(is.numeric, funs(median, mean))

####################################################################
# T test
####################################################################
a_users_logs <- rbind(c_users_logs, i_users_logs)
metrics = c("totalCards", "pointsInCard", "coinsInCard", "timeInCards",
            "interactions", "days", "period", "decks", "interactions_day",
            "days_between_sessions")
n_metrics = c("Total cards", "Total points", "Total coins", "Time in cards",
          "Interactions", "Days using app", "Period of use", "Decks",
          "AIPD", "ATBS")
dat <-data.frame(group=character(),
                 N=integer(),
                 mean=double(),
                 sd=double(),
                 se=double(),
                 ci=double(),
                 metric=character())
for(i in 1:10){
  c_dat <- summarySE(a_users_logs, measurevar=metrics[i], groupvars=c("group"))
  c_dat$metric <- n_metrics[i]
  colnames(c_dat)[3] <- "mean"
  dat <- rbind(dat, c_dat)
}
colnames(dat)[1] <- "Group"

ggplot(dat, aes(x=Group, y=mean, fill=Group))+
  geom_bar(position=position_dodge(), stat="identity")+
  geom_errorbar(aes(ymin=mean-ci, ymax=mean+ci, fill=Group),
                width=.2,
                position=position_dodge(.9))+
  scale_fill_brewer(palette="OrRd") +
  facet_wrap(~metric, scales="free", ncol=3) +
  scale_colour_discrete(name = "") +
  theme_linedraw() +
  theme_light() +
  theme(legend.position="top") +
  theme(strip.text.x = element_text(color="black", size=16),
        legend.text=element_text(size=16),
        axis.text.x=element_blank(),
        axis.text.y=element_text(size=16),
        axis.title.y=element_blank(),
        axis.title.x=element_blank(),
        axis.ticks = element_blank())

t.test(data=a_users_logs, totalCards ~ group, conf.level=0.95)
t.test(data=a_users_logs, pointsInCard ~ group, conf.level=0.95)
t.test(data=a_users_logs, coinsInCard ~ group, conf.level=0.95)
t.test(data=a_users_logs, timeInCards ~ group, conf.level=0.95)
t.test(data=a_users_logs, interactions ~ group, conf.level=0.95)
t.test(data=a_users_logs, days ~ group, conf.level=0.95)
t.test(data=a_users_logs, period ~ group, conf.level=0.95)
t.test(data=a_users_logs, decks ~ group, conf.level=0.95)
t.test(data=a_users_logs, interactions_day ~ group, conf.level=0.95)
t.test(data=a_users_logs, days_between_sessions ~ group, conf.level=0.95)
