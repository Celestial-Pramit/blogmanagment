package com.blogmanage.blogmanagement.config;

import com.blogmanage.blogmanagement.model.BlogPost;
import com.blogmanage.blogmanagement.model.User;
import com.blogmanage.blogmanagement.model.Visibility;
import com.blogmanage.blogmanagement.repository.BlogPostRepository;
import com.blogmanage.blogmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BlogPostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (postRepository.count() > 0) {
            return;
        }

        User author = userRepository.findByEmail("editorial@inkwell.dev")
                .orElseGet(() -> userRepository.save(User.builder()
                        .email("editorial@inkwell.dev")
                        .passwordHash(passwordEncoder.encode("inkwell-demo-2026"))
                        .displayName("Inkwell Editorial")
                        .createdAt(Instant.now())
                        .build()));

        Instant now = Instant.now();

        List<BlogPost> seedPosts = List.of(
                BlogPost.builder()
                        .title("The Case for Writing in Public")
                        .slug("the-case-for-writing-in-public")
                        .excerpt("Publishing half-formed ideas is uncomfortable — and that discomfort is exactly the point.")
                        .content("There's a particular kind of fear that comes from hitting publish on an idea you're not sure about yet.\n\nBut the discomfort is doing something useful: it forces precision. A thought that stays in your head can hide its gaps forever. The same thought, written for a stranger, has nowhere to hide.\n\nThis isn't a call to publish everything. It's a case for lowering the bar on what counts as 'ready.'")
                        .coverImageUrl("https://images.unsplash.com/photo-1455390582262-044cdead277a?w=1200&q=80")
                        .tags(List.of("writing", "process"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(1, ChronoUnit.DAYS))
                        .updatedAt(now.minus(1, ChronoUnit.DAYS))
                        .visibility(Visibility.PUBLIC)
                        .build(),

                BlogPost.builder()
                        .title("Notes on Slow Software")
                        .slug("notes-on-slow-software")
                        .excerpt("Most tools optimize for speed of shipping. Few optimize for speed of understanding.")
                        .content("We've gotten very good at shipping fast and very bad at building things people can hold in their heads.\n\nSlow software isn't about being sluggish — it's about being legible. A codebase you can explain in five minutes is worth more than one that runs five milliseconds faster.\n\nThe fastest team isn't the one that ships the most commits. It's the one that spends the least time confused.")
                        .coverImageUrl("https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=800&q=80")
                        .tags(List.of("software", "engineering"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(3, ChronoUnit.DAYS))
                        .updatedAt(now.minus(3, ChronoUnit.DAYS))
                        .visibility(Visibility.PUBLIC)
                        .build(),

                BlogPost.builder()
                        .title("A Private Draft on Career Pivots")
                        .slug("a-private-draft-on-career-pivots")
                        .excerpt("Not ready for public consumption yet — thinking out loud about what comes next.")
                        .content("This one's still rough. Sharing it with signed-in readers only while I figure out what I actually think.\n\nThe honest version: most career pivots look reckless right before they look obvious in hindsight. The trick is tolerating the 'reckless' phase long enough to find out which one you're in.")
                        .coverImageUrl("https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800&q=80")
                        .tags(List.of("career", "drafts"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(5, ChronoUnit.DAYS))
                        .updatedAt(now.minus(5, ChronoUnit.DAYS))
                        .visibility(Visibility.PRIVATE)
                        .build(),

                BlogPost.builder()
                        .title("Why Most Meetings Should Be a Paragraph")
                        .slug("why-most-meetings-should-be-a-paragraph")
                        .excerpt("If it can be written, it shouldn't be a meeting. If it can't, it probably shouldn't be a meeting either.")
                        .content("Meetings are the default because writing is harder than talking. That's exactly why writing is usually the better choice.\n\nA well-written paragraph forces you to resolve ambiguity before anyone else's time is spent. A meeting lets ambiguity survive, sometimes for hours.")
                        .coverImageUrl("https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=800&q=80")
                        .tags(List.of("work", "communication"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(7, ChronoUnit.DAYS))
                        .updatedAt(now.minus(7, ChronoUnit.DAYS))
                        .visibility(Visibility.PUBLIC)
                        .build(),

                BlogPost.builder()
                        .title("Reading Old Code")
                        .slug("reading-old-code")
                        .excerpt("The best way to learn a language isn't tutorials — it's reading code someone else was proud of.")
                        .content("Every tutorial teaches you syntax. Almost none teach you taste.\n\nTaste comes from reading code written by people who cared — not perfect code, just code with clear intent. You start noticing patterns: where they chose clarity over cleverness, where they left a comment because the 'why' wasn't obvious.\n\nGo find a well-regarded open source project in whatever you're learning. Read it like a book, not a reference.")
                        .coverImageUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=800&q=80")
                        .tags(List.of("learning", "engineering"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(10, ChronoUnit.DAYS))
                        .updatedAt(now.minus(10, ChronoUnit.DAYS))
                        .visibility(Visibility.PUBLIC)
                        .build(),

                BlogPost.builder()
                        .title("On Keeping a Commonplace Book")
                        .slug("on-keeping-a-commonplace-book")
                        .excerpt("A centuries-old habit — writing down what strikes you — turns out to be exactly what modern note apps are chasing.")
                        .content("Long before 'second brain' apps, writers kept commonplace books — a single notebook for quotes, observations, half-formed ideas, anything worth not losing.\n\nThe habit isn't about the tool. It's about the discipline of writing things down the moment they strike you, before your brain quietly discards them as unimportant.\n\nThis blog is, in a small way, a public commonplace book.")
                        .coverImageUrl("https://images.unsplash.com/photo-1455390582262-044cdead277a?w=800&q=80")
                        .tags(List.of("writing", "habits"))
                        .authorId(author.getId())
                        .authorName(author.getDisplayName())
                        .createdAt(now.minus(14, ChronoUnit.DAYS))
                        .updatedAt(now.minus(14, ChronoUnit.DAYS))
                        .visibility(Visibility.PUBLIC)
                        .build()
        );

        postRepository.saveAll(seedPosts);
    }
}