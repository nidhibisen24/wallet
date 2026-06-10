from rest_framework import serializers
from .models import User ,Wallet , FundRequest , QRCode
from django.contrib.auth import authenticate
from rest_framework import serializers

from rest_framework import serializers
from .models import User


class RegisterSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = [
            'full_name',
            'mobile_number',
            'password'
        ]

        extra_kwargs = {
            'password': {'write_only': True}
        }


class FundRequestSerializer(serializers.ModelSerializer):

    user_name = serializers.CharField(
        source='user.full_name',
        read_only=True
    )

    mobile_number = serializers.CharField(
        source='user.mobile_number',
        read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
            'id',
            'user',
            'user_name',
            'mobile_number',
            'amount',
            'request_type',
            'status',
            'created_at'
        ]


class UserListSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'role',
            'wallet_balance'
        ]

class UserDetailSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    total_requests = serializers.SerializerMethodField()

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'role',
            'wallet_balance',
            'total_requests'
        ]

    def get_total_requests(self, obj):
        return obj.fund_requests.count()
    

class UserRequestHistorySerializer(serializers.ModelSerializer):

    class Meta:
        model = FundRequest
        fields = [
            'id',
            'amount',
            'request_type',
            'status',
            'created_at'
        ]


class UserDashboardSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    total_requests = serializers.SerializerMethodField()
    pending_requests = serializers.SerializerMethodField()

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'wallet_balance',
            'total_requests',
            'pending_requests'
        ]

    def get_total_requests(self, obj):
        return obj.fund_requests.count()

    def get_pending_requests(self, obj):
        return obj.fund_requests.filter(
            status='PENDING'
        ).count()
    

class QRCodeSerializer(serializers.ModelSerializer):

    class Meta:
        model = QRCode
        fields = [
            'id',
            'image'
        ]

class TransactionHistorySerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(
        source="user.full_name",
        read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
            "id",
            "user_id",
            "user_name",
            "amount",
            "request_type",
            "status",
            "created_at"
        ]

class FundApprovedSerializers(serializers.ModelSerializer):

    user_name = serializers.CharField(
        source='user.full_name',
        read_only=True
    )

    mobile_number = serializers.CharField(
        source='user.mobile_number',
        read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
            'id',
            'user',
            'user_name',
            'mobile_number',
            'amount',
            'request_type',
            'status',
            'created_at'
        ]