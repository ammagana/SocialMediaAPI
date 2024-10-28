package com.cooksys.project1.services;

import com.cooksys.project1.dtos.*;
import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.entities.HashTag;
import com.cooksys.project1.entities.Tweet;
import com.cooksys.project1.entities.User;
import com.cooksys.project1.exceptions.BadRequestException;
import com.cooksys.project1.exceptions.NotAuthorizedException;
import com.cooksys.project1.exceptions.NotFoundException;
import com.cooksys.project1.mappers.CredentialsMapper;
import com.cooksys.project1.mappers.HashtagMapper;
import com.cooksys.project1.mappers.TweetMapper;
import com.cooksys.project1.mappers.UserMapper;
import com.cooksys.project1.repositories.HashtagRepository;
import com.cooksys.project1.repositories.TweetRepository;
import com.cooksys.project1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CredentialsMapper credentialsMapper;
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final Pattern mentionPattern = Pattern.compile("@([a-zA-Z0-9_]+)");
    private final Pattern hashtagPattern = Pattern.compile("#([a-zA-Z0-9_]+)");

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        Credentials userCredentials = credentialsMapper.dtoToCredentials(tweetRequestDto.getCredentials());
        Optional<User> potentialUser = userRepository.findByCredentials(userCredentials);
        if (potentialUser.isEmpty()) {
            throw new BadRequestException("incorrect credentials");
        }
        User author = potentialUser.get();
        if (author.getDeleted() != null && author.getDeleted()) {
            throw new NotAuthorizedException("your account is deleted");
        }
        if(tweetRequestDto.getContent() == null){
            throw new BadRequestException("you need to have content to post");
        }

        Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);

        String content = tweetToCreate.getContent();

        Matcher mentionMatcher = mentionPattern.matcher(content);
        List<User> mentionedUsers = new ArrayList<>();

        while (mentionMatcher.find()) {
            String username = mentionMatcher.group(1);
            userRepository.findByCredentialsUsername(username).ifPresent(mentionedUsers::add);
        }

        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        List<HashTag> hashtags = new ArrayList<>();

        while (hashtagMatcher.find()) {
            String hashtagLabel = hashtagMatcher.group(1);
            hashtagRepository.findByLabel(hashtagLabel).ifPresentOrElse(hashtags::add,
                    () -> {
                        HashTag newHashtag = new HashTag();
                        newHashtag.setLabel(hashtagLabel);
                        newHashtag.setFirstUsed(Timestamp.valueOf(tweetToCreate.getPosted()));
                        hashtags.add(newHashtag);
                    });
        }

        for (HashTag tag : hashtags) {
            tag.setLastUsed(Timestamp.valueOf(tweetToCreate.getPosted()));
            hashtagRepository.saveAndFlush(tag);
        }

        tweetToCreate.setAuthor(author);
        tweetToCreate.setContent(content);
        tweetToCreate.setMentionedUsers(mentionedUsers);
        tweetToCreate.setHashtags(hashtags);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToCreate));
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweet = potentialTweet.get();
        if (tweet.getDeleted()) {
            throw new BadRequestException("that tweet was deleted");
        }
        return tweetMapper.entityToDto(tweet);
    }

    @Override
    public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
        Credentials userCredentials = credentialsMapper.dtoToCredentials(credentialsDto);
        Optional<User> potentialUser = userRepository.findByCredentials(userCredentials);
        if (potentialUser.isEmpty()) {
            throw new BadRequestException("incorrect credentials");
        }
        User user = potentialUser.get();
        if (user.getDeleted() != null && user.getDeleted()) {
            throw new NotAuthorizedException("your account is deleted");
        }
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweetToDelete = potentialTweet.get();
        if (tweetToDelete.getDeleted()) {
            throw new BadRequestException("this tweet is already deleted");
        }
        if (!tweetToDelete.getAuthor().equals(user)) {
            throw new NotAuthorizedException("you are not the poster of the tweet");
        }
        tweetToDelete.setDeleted(true);
        tweetRepository.saveAndFlush(tweetToDelete);
        return tweetMapper.entityToDto(tweetToDelete);

    }

    @Override
    public List<HashtagDto> getTweetTags(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweetToGetTags = potentialTweet.get();
        if (tweetToGetTags.getDeleted()) {
            throw new BadRequestException("the tweet was deleted");
        }
        return hashtagMapper.entitiesToDtos(tweetToGetTags.getHashtags());
    }

    @Override
    public List<UserResponseDto> getUserMentions(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweetToGetMentions = potentialTweet.get();
        if (tweetToGetMentions.getDeleted()) {
            throw new BadRequestException("that tweet was deleted");
        }
        List<User> mentioned = new ArrayList<>();
        for (User user : tweetToGetMentions.getMentionedUsers()) {
            if (user.getDeleted() == null || !user.getDeleted()) {
                mentioned.add(user);
            }
        }
        return userMapper.entitiesToDtos(mentioned);
    }

    @Override
    public List<TweetResponseDto> getTweetReplies(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweetToGetReplies = potentialTweet.get();
        if (tweetToGetReplies.getDeleted()) {
            throw new BadRequestException("that tweet was deleted");
        }
        List<Tweet> replies = new ArrayList<>();
        for (Tweet reply : tweetToGetReplies.getReplies()) {
            //will always be set to true or false
            if (!reply.getDeleted()) {
                replies.add(reply);
            }
        }
        return tweetMapper.entitiesToDtos(replies);
    }

    @Override
    public List<TweetResponseDto> getTweetReposts(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with that id");
        }
        Tweet tweetToGetReposts = potentialTweet.get();
        if (tweetToGetReposts.getDeleted()) {
            throw new BadRequestException("that tweet was deleted");
        }
        return tweetMapper.entitiesToDtos(tweetToGetReposts.getReposts());
    }

    @Override
    public void likeTweet(Long id, CredentialsDto credentialsDto) {
        Credentials userCredentials = credentialsMapper.dtoToCredentials(credentialsDto);
        Optional<User> potentialUser = userRepository.findByCredentials(userCredentials);
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialUser.isEmpty()) {
            throw new NotAuthorizedException("incorrect credentials");
        }
        User user = potentialUser.get();
        if (user.getDeleted() != null && user.getDeleted()) {
            throw new NotAuthorizedException("user has been deleted");
        }
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet with id found");
        }
        Tweet tweetToLike = potentialTweet.get();
        if (tweetToLike.getDeleted()) {
            throw new NotFoundException("tweet has been deleted");
        }

        if (!user.getLikedTweets().contains(tweetToLike)) {
            user.getLikedTweets().add(tweetToLike);
            tweetToLike.getLikedByUsers().add(user);
        }

        userRepository.saveAndFlush(user);
//        tweetRepository.saveAndFlush(tweetToLike);
    }

    @Override
    public List<UserResponseDto> likedBy(Long id) {
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet with id found");
        }
        Tweet tweet = potentialTweet.get();
        if (tweet.getDeleted()) {
            throw new BadRequestException("tweet has been deleted");
        }
        return userMapper.entitiesToDtos(tweet.getLikedByUsers());
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        Credentials userCredentials = credentialsMapper.dtoToCredentials(credentialsDto);
        Optional<User> potentialUser = userRepository.findByCredentials(userCredentials);
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialUser.isEmpty()) {
            throw new NotAuthorizedException("incorrect credentials");
        }
        User user = potentialUser.get();
        if (user.getDeleted() != null && user.getDeleted()) {
            throw new NotAuthorizedException("user has been deleted");
        }
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet with id found");
        }
        Tweet tweet = potentialTweet.get();
        if (tweet.getDeleted()) {
            throw new NotFoundException("tweet has been deleted");
        }
        Tweet newTweet = new Tweet();
        newTweet.setAuthor(user);
        newTweet.setRepostOf(tweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
    }

    @Override
    public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
        Credentials userCredentials = credentialsMapper.dtoToCredentials(tweetRequestDto.getCredentials());
        Optional<User> potentialUser = userRepository.findByCredentials(userCredentials);
        Optional<Tweet> potentialTweet = tweetRepository.findById(id);
        if (potentialUser.isEmpty()) {
            throw new NotAuthorizedException("incorrect credentials");
        }
        User author = potentialUser.get();
        if (author.getDeleted() != null && author.getDeleted()) {
            throw new NotAuthorizedException("incorrect credentials");
        }
        if (potentialTweet.isEmpty()) {
            throw new NotFoundException("no tweet with id found");
        }
        Tweet originalTweet = potentialTweet.get();
        if (originalTweet.getDeleted()) {
            throw new BadRequestException("original tweet has been deleted");
        }

        String content = tweetRequestDto.getContent();

        Tweet newTweet = new Tweet();
        newTweet.setAuthor(author);
        newTweet.setContent(content);
        newTweet.setInReplyTo(originalTweet);


        Matcher mentionMatcher = mentionPattern.matcher(content);
        List<User> mentionedUsers = new ArrayList<>();

        while (mentionMatcher.find()) {
            String username = mentionMatcher.group(1);
            userRepository.findByCredentialsUsername(username).ifPresent(mentionedUsers::add);
        }

        Matcher hashtagMatcher = hashtagPattern.matcher(content);
        List<HashTag> hashtags = new ArrayList<>();

        while (hashtagMatcher.find()) {
            String hashtagLabel = hashtagMatcher.group(1);
            hashtagRepository.findByLabel(hashtagLabel).ifPresentOrElse(hashtags::add,
                    () -> {
                        HashTag newHashtag = new HashTag();
                        newHashtag.setLabel(hashtagLabel);
                        newHashtag.setFirstUsed(Timestamp.valueOf(newTweet.getPosted()));
                        hashtags.add(newHashtag);
                    });
        }

        for (HashTag tag : hashtags) {
            tag.setLastUsed(Timestamp.valueOf(newTweet.getPosted()));
            hashtagRepository.saveAndFlush(tag);
        }

        newTweet.setMentionedUsers(mentionedUsers);
        newTweet.setHashtags(hashtags);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(newTweet));
    }

    @Override
    public ContextDto getContext(Long id) {
        Optional<Tweet> optionalTweet = tweetRepository.findById(id);
        if (optionalTweet.isEmpty()) {
            throw new NotFoundException("no tweet found with id: " + id);
        }
        Tweet target = optionalTweet.get();
        if (target.getDeleted()) {
            throw new NotFoundException("target tweet was deleted");
        }
        List<Tweet> before = new ArrayList<>();
        Tweet current = target.getInReplyTo();
        while (current != null) {
            if (!current.getDeleted()) {
                before.add(current);
            }
            current = current.getInReplyTo();
        }
        Collections.reverse(before);

        List<Tweet> after = getRepliesAfter(target);

        ContextDto context = new ContextDto();
        context.setTarget(tweetMapper.entityToDto(target));
        context.setBefore(tweetMapper.entitiesToDtos(before));
        context.setAfter(tweetMapper.entitiesToDtos(after));
        return context;
    }

    private List<Tweet> getRepliesAfter(Tweet tweet) {
        List<Tweet> replies = new ArrayList<>();
        for (Tweet reply : tweet.getReplies()) {
            if (!reply.getDeleted()) {
                replies.add(reply);
                replies.addAll(getRepliesAfter(reply));
            } else {
                replies.addAll(getRepliesAfter(reply));
            }
        }
        replies.sort(Comparator.comparing(Tweet::getPosted));
        return replies;
    }

}
