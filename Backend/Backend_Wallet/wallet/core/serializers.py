from rest_framework import serializers
from .models import User ,Wallet
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



