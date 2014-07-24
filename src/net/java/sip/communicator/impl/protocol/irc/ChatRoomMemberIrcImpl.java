/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.irc;

import java.util.*;

import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.util.*;

/**
 * Represents a chat room member.
 *
 * @author Stephane Remy
 * @author Lubomir Marinov
 * @author Danny van Heumen
 */
public class ChatRoomMemberIrcImpl
    implements ChatRoomMember
{
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger
        .getLogger(ChatRoomMemberIrcImpl.class);

    /**
     * The ChatRoom.
     */
    private final ChatRoom chatRoom;

    /**
     * The id of the contact.
     */
    private String contactID;

    /**
     * The provider that created us.
     */
    private final ProtocolProviderService parentProvider;

    /**
     * Set of active roles.
     */
    private final SortedSet<ChatRoomMemberRole> roles =
        new TreeSet<ChatRoomMemberRole>();

    /**
     * Creates an instance of <tt>ChatRoomMemberIrcImpl</tt>, by specifying the
     * protocol provider, the corresponding chat room, where this member is
     * joined, the identifier of the contact (the nickname), the login, the
     * host name and finally the role that this contact has in the chat room.
     *
     * @param parentProvider the protocol provider, to which the corresponding
     * chat room belongs
     * @param chatRoom the chat room, where this member is joined
     * @param contactID the nickname of the member
     * @param chatRoomMemberRole the role that this member has in the
     * corresponding chat room
     */
    public ChatRoomMemberIrcImpl(final ProtocolProviderService parentProvider,
        final ChatRoom chatRoom, final String contactID,
        final ChatRoomMemberRole chatRoomMemberRole)
    {
        if (parentProvider == null)
        {
            throw new IllegalArgumentException(
                "parent protocol provider cannot be null");
        }
        this.parentProvider = parentProvider;
        if (chatRoom == null)
        {
            throw new IllegalArgumentException(
                "chat room instance cannot be null");
        }
        this.chatRoom = chatRoom;
        if (contactID == null)
        {
            throw new IllegalArgumentException("contact ID cannot be null");
        }
        this.contactID = contactID;
        if (chatRoomMemberRole == null)
        {
            throw new IllegalArgumentException("member role cannot be null");
        }
        this.roles.add(chatRoomMemberRole);
    }

    /**
     * Returns the chat room that this member is participating in.
     *
     * @return the <tt>ChatRoom</tt> instance that this member belongs to.
     */
    public ChatRoom getChatRoom()
    {
        return this.chatRoom;
    }

    /**
     * Returns the protocol provider instance that this member has originated
     * in.
     *
     * @return the <tt>ProtocolProviderService</tt> instance that created this
     * member and its containing chat room
     */
    public ProtocolProviderService getProtocolProvider()
    {
        return this.parentProvider;
    }

    /**
     * Returns the contact identifier representing this contact. For IRC
     * this method returns the same as getName().
     *
     * @return a String (contact address), uniquely representing the contact
     * over the service being used by the associated protocol provider instance
     */
    public String getContactAddress()
    {
        return this.contactID;
    }

    /**
     * Returns the name of this member as it is known in its containing
     * chat room (i.e. a nickname). The name returned by this method, may
     * sometimes match the string returned by getContactID() which is actually
     * the address of a contact in the realm of the corresponding protocol.
     *
     * @return the name of this member as it is known in the containing chat
     * room (i.e. a nickname).
     */
    public String getName()
    {
        return this.contactID;
    }

    /**
     * Set a new name for this ChatRoomMember.
     *
     * @param newName new name to set for chat room member
     */
    public void setName(final String newName)
    {
        if (newName == null)
        {
            throw new IllegalArgumentException("newName cannot be null");
        }
        this.contactID = newName;
    }

    /**
     * Returns the role of this chat room member in its containing room.
     *
     * @return a <tt>ChatRoomMemberRole</tt> instance indicating the role
     * the this member in its containing chat room.
     */
    public ChatRoomMemberRole getRole()
    {
        return this.roles.first();
    }

    /**
     * Sets a new member role to this <tt>ChatRoomMember</tt>.
     *
     * @param chatRoomMemberRole the role to be set
     */
    public void setRole(final ChatRoomMemberRole chatRoomMemberRole)
    {
        // Ignore explicit set role operations, since we only allow
        // modifications from the IRC server.
        LOGGER.debug("Ignoring request to set member role.");
        return;
    }

    /**
     * Add a role.
     *
     * @param role the new role
     */
    void addRole(final ChatRoomMemberRole role)
    {
        this.roles.add(role);
    }

    /**
     * Remove a role.
     *
     * @param role the revoked role
     */
    void removeRole(final ChatRoomMemberRole role)
    {
        this.roles.remove(role);
    }

    /**
     * Returns null to indicate that there's no avatar attached to the IRC
     * member.
     *
     * @return null
     */
    public byte[] getAvatar()
    {
        return null;
    }

    /**
     * Returns null to indicate that there's no contact corresponding to the
     * IRC member.
     *
     * @return null
     */
    public Contact getContact()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((contactID == null) ? 0 : contactID.hashCode());
        result =
            prime * result
                + ((parentProvider == null) ? 0 : parentProvider.hashCode());
        return result;
    }

    /**
     * equality by provider protocol instance and contact ID.
     *
     * Enables the possibility to check if chat room member is same member in
     * different chat rooms. Values are only reliable for the same connection,
     * so also check protocol provider instance.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChatRoomMemberIrcImpl other = (ChatRoomMemberIrcImpl) obj;
        if (!contactID.equals(other.contactID))
            return false;
        if (!parentProvider.equals(other.parentProvider))
            return false;
        return true;
    }
}
