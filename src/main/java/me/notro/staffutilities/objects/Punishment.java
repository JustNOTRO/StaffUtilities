package me.notro.staffutilities.objects;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@NonNull
public class Punishment {

    private final String requester;
    private final UUID target;

    @Setter
    private String reason;
}
